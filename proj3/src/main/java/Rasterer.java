import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    /* the longitude distance per pixel of each depth.*/
    double[] lonDPPs;
    /* the latitude distance per pixel of each depth.*/
    double[] latDPPs;
    public static final int DEPTH_NUMBER = 8;
    double[] lonPerTiles;
    double[] latPerTiles;

    public Rasterer() {
        double latDPP = Math.abs(MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) / MapServer.TILE_SIZE;
        double lonDPP = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        lonDPPs = new double[DEPTH_NUMBER];
        latDPPs = new double[DEPTH_NUMBER];
        for (int m = 0; m < DEPTH_NUMBER; m += 1) {
            lonDPPs[m] = lonDPP;
            lonDPP = lonDPP / 2;
            latDPPs[m] = latDPP;
            latDPP = latDPP / 2;
        }

        lonPerTiles = new double[DEPTH_NUMBER];
        latPerTiles = new double[DEPTH_NUMBER];
        double initLonPerTile = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON);
        double initLatPerTile = Math.abs(MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT);
        for (int m = 0; m < DEPTH_NUMBER; m += 1) {
            lonPerTiles[m] = initLonPerTile;
            latPerTiles[m] = initLatPerTile;
            initLonPerTile = initLonPerTile / 2;
            initLatPerTile = initLatPerTile / 2;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {

        Map<String, Object> results = new HashMap<>();
        int depth = findDepth(params);
        results.put("depth", depth);
        int x1 = findX(params.get("ullon"), depth);
        int y1 = findY(params.get("ullat"), depth);
        int x2 = findX(params.get("lrlon"), depth);
        int y2 = findY(params.get("lrlat"), depth);
        double raster_ul_lon = MapServer.ROOT_ULLON + x1 * lonPerTiles[depth];
        double raster_ul_lat = MapServer.ROOT_ULLAT - y1 * latPerTiles[depth];
        double raster_lr_lon = MapServer.ROOT_ULLON + (x2 + 1) * lonPerTiles[depth];
        double raster_lr_lat = MapServer.ROOT_ULLAT - (y2 + 1) * latPerTiles[depth];
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("render_grid", renderFiles(depth, x1, y1, x2, y2));
        boolean query_success = !(params.get("ullon") >= MapServer.ROOT_LRLON)
                && !(params.get("ullat") <= MapServer.ROOT_LRLAT)
                && !(params.get("lrlon") <= MapServer.ROOT_ULLON)
                && !(params.get("lrlat") >= MapServer.ROOT_ULLAT);
        results.put("query_success", query_success);
        return results;
    }

    private void checkBounds(Map<String, Double> params) {
        if (params.get("ullon") > params.get("lrlon")) {
            double temp = params.get("ullon");
            params.put("ullon", params.get("lrlon"));
            params.put("lrlon", temp);
        }
        if (params.get("ullat") < params.get("lrlat")) {
            double temp = params.get("ullat");
            params.put("ullat", params.get("lrlat"));
            params.put("lrlat", temp);
        }
        if (params.get("ullon") < MapServer.ROOT_ULLON) {
            params.put("ullon", MapServer.ROOT_ULLON);
        }
        if (params.get("ullat") > MapServer.ROOT_ULLAT) {
            params.put("ullat", MapServer.ROOT_ULLAT);
        }
        if (params.get("lrlon") > MapServer.ROOT_LRLON) {
            params.put("lrlon", MapServer.ROOT_LRLON);
        }
        if (params.get("lrlat") < MapServer.ROOT_LRLAT) {
            params.put("lrlat", MapServer.ROOT_LRLAT);
        }

    }

    /**
     * given the params return the proper depth that satisfy the need of resolution.
     * @param params the params from the web page
     * @return the proper depth
     */
    private int findDepth(Map<String, Double> params) {
        double LonDPP = Math.abs(params.get("ullon") - params.get("lrlon")) / params.get("w");
        double LatDPP = Math.abs(params.get("ullat") - params.get("lrlat")) / params.get("h");
        int depth = 0;
        for (int m = 0; m < DEPTH_NUMBER; m += 1) {
            depth = m;
            if (lonDPPs[m] < LonDPP) {
                break;
            }
        }
        return depth;
    }

    /**
     * given a longitude of a point and the depth find the x index of the image that the point is in.
     * for example if the point is in d1_x4_y5.png, then the function returns 4.
     * @param longitude the longitude of the image.
     * @param depth the depth used.
     * @return the x index of the image that the point is in.
     */
    private int findX(double longitude, int depth) {
        double lonPerTile = lonPerTiles[depth];
        int res = (int) ((longitude - MapServer.ROOT_ULLON) / lonPerTile);
        res = Math.max(0, res);
        if (res >= (1 << depth)) {
            res = (1 << depth) - 1;
        }
        return res;
    }

    /**
     * same as the findX function, but instead return the y index.
     * @param latitude the latitude of the image.
     * @param depth the depth used.
     * @return the index of the image that the point is in.
     */
    private int findY(double latitude, int depth) {
        double latPerTile = latPerTiles[depth];
        int res = (int) ((MapServer.ROOT_ULLAT - latitude) / latPerTile);
        res = Math.max(0, res);
        if (res >= (1 << depth)) {
            res = (1 << depth) - 1;
        }
        return res;
    }

    private String[][] renderFiles(int depth, int x1, int y1, int x2, int y2) {
        int xSize = Math.abs(x2 - x1 + 1);
        int ySize = Math.abs(y2 - y1 + 1);
        String[][] result = new String[ySize][xSize];
        for (int m = 0; m < ySize; m += 1) {
            for (int n = 0; n < xSize; n += 1) {
                result[m][n] = "d" + depth + "_x" + (n + x1) + "_y" + (m + y1) + ".png";
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Rasterer rasterer = new Rasterer();
    }
}
