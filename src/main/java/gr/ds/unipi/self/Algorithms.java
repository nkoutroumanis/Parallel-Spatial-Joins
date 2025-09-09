package gr.ds.unipi.self;

import scala.Tuple2;
import scala.Tuple3;

import java.util.List;

public class Algorithms {
    public static void planeSweepSelf(Tuple3<String, Double, Double>[] objectsA, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){

        Tuple3<String, Double, Double>[] objectsB = objectsA;

        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._2()-(radius/2)<objectsB[indexB]._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._2()+(radius/2)>=objectsB[innerIndexB]._2()-(radius/2))){

                    if(indexA!=innerIndexB && (Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[indexA], objectsB[innerIndexB]));
                    }
                    innerIndexB++;
                }

                indexA++;

                //extra command for self join
                indexB++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._2()+(radius/2)>=objectsA[innerIndexA]._2()-(radius/2))){

                    if(innerIndexA!=indexB && (Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[innerIndexA], objectsB[indexB]));
                    }
                    innerIndexA++;
                }
                indexB++;

                //extra command for self join
                indexA++;
            }
        }
    }

    public static long planeSweepSelf(Tuple3<String, Double, Double>[] objectsA, double radius){

        long counter =0;
        Tuple3<String, Double, Double>[] objectsB = objectsA;

        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._2()-(radius/2)<objectsB[indexB]._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._2()+(radius/2)>=objectsB[innerIndexB]._2()-(radius/2))){

                    if(indexA!=innerIndexB && (Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexB++;
                }

                indexA++;

                //extra command for self join
                indexB++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._2()+(radius/2)>=objectsA[innerIndexA]._2()-(radius/2))){

                    if(innerIndexA!=indexB && (Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexA++;
                }
                indexB++;

                //extra command for self join
                indexA++;
            }
        }
        return counter;
    }

    public static void planeSweep(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._2()-(radius/2)<objectsB[indexB]._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._2()+(radius/2)>=objectsB[innerIndexB]._2()-(radius/2))){

                    if((Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[indexA], objectsB[innerIndexB]));
                    }
                    innerIndexB++;
                }
                indexA++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._2()+(radius/2)>=objectsA[innerIndexA]._2()-(radius/2))){

                    if((Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[innerIndexA], objectsB[indexB]));
                    }
                    innerIndexA++;
                }
                indexB++;
            }
        }
    }

    public static long planeSweep(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, double radius){
        long counter = 0;
        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._2()-(radius/2)<objectsB[indexB]._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._2()+(radius/2)>=objectsB[innerIndexB]._2()-(radius/2))){

                    if((Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexB++;
                }
                indexA++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._2()+(radius/2)>=objectsA[innerIndexA]._2()-(radius/2))){

                    if((Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexA++;
                }
                indexB++;
            }
        }
        return counter;
    }

    public static void planeSweepSelfOnY(Tuple3<String, Double, Double>[] objectsA, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){

        Tuple3<String, Double, Double>[] objectsB = objectsA;

        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._3()-(radius/2)<objectsB[indexB]._3()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._3()+(radius/2)>=objectsB[innerIndexB]._3()-(radius/2))){

                    if(indexA!=innerIndexB && (Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[indexA], objectsB[innerIndexB]));
                    }
                    innerIndexB++;
                }

                indexA++;

                //extra command for self join
                indexB++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._3()+(radius/2)>=objectsA[innerIndexA]._3()-(radius/2))){

                    if(innerIndexA!=indexB && (Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[innerIndexA], objectsB[indexB]));
                    }
                    innerIndexA++;
                }
                indexB++;

                //extra command for self join
                indexA++;
            }
        }
    }

    public static long planeSweepSelfOnY(Tuple3<String, Double, Double>[] objectsA, double radius){

        long counter = 0;
        Tuple3<String, Double, Double>[] objectsB = objectsA;

        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._3()-(radius/2)<objectsB[indexB]._3()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._3()+(radius/2)>=objectsB[innerIndexB]._3()-(radius/2))){

                    if(indexA!=innerIndexB && (Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexB++;
                }

                indexA++;

                //extra command for self join
                indexB++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._3()+(radius/2)>=objectsA[innerIndexA]._3()-(radius/2))){

                    if(innerIndexA!=indexB && (Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexA++;
                }
                indexB++;

                //extra command for self join
                indexA++;
            }
        }
        return counter;
    }

    public static void planeSweepOnY(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._3()-(radius/2)<objectsB[indexB]._3()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._3()+(radius/2)>=objectsB[innerIndexB]._3()-(radius/2))){

                    if((Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[indexA], objectsB[innerIndexB]));
                    }
                    innerIndexB++;
                }
                indexA++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._3()+(radius/2)>=objectsA[innerIndexA]._3()-(radius/2))){

                    if((Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA[innerIndexA], objectsB[indexB]));
                    }
                    innerIndexA++;
                }
                indexB++;
            }
        }
    }

    public static long planeSweepOnY(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, double radius){

        long counter = 0;
        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.length && indexB<objectsB.length){
            if(objectsA[indexA]._3()-(radius/2)<objectsB[indexB]._3()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.length && (objectsA[indexA]._3()+(radius/2)>=objectsB[innerIndexB]._3()-(radius/2))){

                    if((Math.pow((objectsA[indexA]._2() - objectsB[innerIndexB]._2()), 2) + Math.pow((objectsA[indexA]._3()- objectsB[innerIndexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexB++;
                }
                indexA++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.length && (objectsB[indexB]._3()+(radius/2)>=objectsA[innerIndexA]._3()-(radius/2))){

                    if((Math.pow((objectsA[innerIndexA]._2() - objectsB[indexB]._2()), 2) + Math.pow((objectsA[innerIndexA]._3()- objectsB[indexB]._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        counter++;
                    }
                    innerIndexA++;
                }
                indexB++;
            }
        }
        return counter;
    }

    public static void bruteForceSelf(Tuple3<String, Double, Double>[] objectsA, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        Tuple3<String, Double, Double>[] objectsB = objectsA;
        for (int i = 0; i < objectsA.length-1; i++) {
            for (int j = i+1; j < objectsB.length; j++) {
                if((Math.pow((objectsA[i]._2() - objectsB[j]._2()), 2) + Math.pow((objectsA[i]._3()- objectsB[j]._3()), 2) <= Math.pow(radius, 2))){
                    results.add(Tuple2.apply(objectsA[i], objectsB[j]));
                }
            }
        }
    }

    public static long bruteForceSelf(Tuple3<String, Double, Double>[] objectsA, double radius){
        int counter = 0;
        Tuple3<String, Double, Double>[] objectsB = objectsA;
        for (int i = 0; i < objectsA.length-1; i++) {
            for (int j = i+1; j < objectsB.length; j++) {
                if((Math.pow((objectsA[i]._2() - objectsB[j]._2()), 2) + Math.pow((objectsA[i]._3()- objectsB[j]._3()), 2) <= Math.pow(radius, 2))){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static void bruteForce(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        for (int i = 0; i < objectsA.length; i++) {
            for (int j = 0; j < objectsB.length; j++) {
                if((Math.pow((objectsA[i]._2() - objectsB[j]._2()), 2) + Math.pow((objectsA[i]._3()- objectsB[j]._3()), 2) <= Math.pow(radius, 2))){
                    results.add(Tuple2.apply(objectsA[i], objectsB[j]));
                }
            }
        }
    }

    public static long bruteForce(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, double radius){
        int counter = 0;
        for (int i = 0; i < objectsA.length; i++) {
            for (int j = 0; j < objectsB.length; j++) {
                if((Math.pow((objectsA[i]._2() - objectsB[j]._2()), 2) + Math.pow((objectsA[i]._3()- objectsB[j]._3()), 2) <= Math.pow(radius, 2))){
                    counter++;
                }
            }
        }
        return counter;
    }

}
