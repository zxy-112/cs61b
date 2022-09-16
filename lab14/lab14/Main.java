package lab14;

import lab14lib.Generator;
import lab14lib.GeneratorAudioAnimator;
import lab14lib.GeneratorAudioVisualizer;
import lab14lib.GeneratorPlayer;

public class Main {
    public static void main(String[] args) {
        Generator generator = new StrangeBitwiseGenerator(1024);
        GeneratorAudioAnimator gaa = new GeneratorAudioAnimator(generator);
        gaa.drawAndPlay(1000, 1000000);
    }
}