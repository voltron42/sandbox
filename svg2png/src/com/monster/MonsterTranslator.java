package com.monster;

import com.rasterize.Rasterize;

import java.io.File;
import java.io.IOException;

/**
 * Created by daniel.johnson on 11/30/2015.
 */
public class MonsterTranslator {
    public static void main(String[] args) throws Rasterize.RasterizerException, IOException {
        String folder = "C:/practice/code/food/monster/svgs";
        File dir = new File(folder);
        System.out.println("exists:"+dir.exists());
        System.out.println("is directory:"+dir.isDirectory());
        String[] files = dir.list();
        System.out.println("file count:"+files.length);
        for (String file : files) {
            System.out.println(file);
            String infile = folder + "/" + file;
            String outfile = "resources/cards/" + file.replace(".svg", ".png");
            Rasterize.svgToPngFileStream(infile, outfile);
        }
    }
}
