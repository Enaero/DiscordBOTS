/**
 * Created by enaero
 */
package com.github.enaero;

import com.github.enaero.bots.EnaeroBOT;

public class Main {
    public static void main(String[] args) {
        EnaeroBOT enaero = new EnaeroBOT();

        new Thread(enaero).start();
    }

}
