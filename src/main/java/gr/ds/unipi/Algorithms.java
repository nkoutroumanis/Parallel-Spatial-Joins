package gr.ds.unipi;

import scala.Tuple2;
import scala.Tuple3;

import java.util.Iterator;
import java.util.List;

public class Algorithms {

    public static long bruteForce(Iterable<Tuple3<String, Double, Double>> objectsA, Iterable<Tuple3<String, Double, Double>> objectsB, double radius){
        long counter=0;
        for (Tuple3<String, Double, Double> objectA : objectsA) {
            for (Tuple3<String, Double, Double> objectB : objectsB) {
                if((Math.pow((objectA._2() - objectB._2()), 2) + Math.pow((objectA._3()- objectB._3()), 2) <= Math.pow(radius, 2))){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static void bruteForce(Iterable<Tuple3<String, Double, Double>> objectsA, Iterable<Tuple3<String, Double, Double>> objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        objectsA.forEach(objectA->{
            objectsB.forEach(objectB->{
                if((Math.pow((objectA._2() - objectB._2()), 2) + Math.pow((objectA._3()- objectB._3()), 2) <= Math.pow(radius, 2))){
                    results.add(Tuple2.apply(objectA, objectB));
                }
            });
        });
    }

    public static void bruteForce(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        for (Tuple3<String, Double, Double> objectA : objectsA) {
            for (Tuple3<String, Double, Double> objectB : objectsB) {
                if((Math.pow((objectA._2() - objectB._2()), 2) + Math.pow((objectA._3()- objectB._3()), 2) <= Math.pow(radius, 2))){
                    results.add(Tuple2.apply(objectA, objectB));
                }
            }
        }
    }

    public static long bruteForce(Tuple3<String, Double, Double>[] objectsA, Tuple3<String, Double, Double>[] objectsB, double radius){
        long counter=0;
        for (Tuple3<String, Double, Double> objectA : objectsA) {
            for (Tuple3<String, Double, Double> objectB : objectsB) {
                if((Math.pow((objectA._2() - objectB._2()), 2) + Math.pow((objectA._3()- objectB._3()), 2) <= Math.pow(radius, 2))){
                    counter++;
                }
            }
        }
        return counter;
    }

    public static void bruteForce(List<Tuple3<String, Double, Double>> objectsA, List<Tuple3<String, Double, Double>> objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        objectsA.forEach(objectA->{
            objectsB.forEach(objectB->{
                if((Math.pow((objectA._2() - objectB._2()), 2) + Math.pow((objectA._3()- objectB._3()), 2) <= Math.pow(radius, 2))){
                    results.add(Tuple2.apply(objectA, objectB));
                }
            });
        });
    }

    public static void planeSweep(List<Tuple3<String, Double, Double>> objectsA, List<Tuple3<String, Double, Double>> objectsB, List<Tuple2<Tuple3<String,Double,Double>, Tuple3<String,Double,Double>>> results, double radius){
        int indexA = 0;
        int indexB = 0;

        while(indexA<objectsA.size() && indexB<objectsB.size()){
            if(objectsA.get(indexA)._2()-(radius/2)<objectsB.get(indexB)._2()-(radius/2)){

                int innerIndexB = indexB;
                while(innerIndexB<objectsB.size() && (objectsA.get(indexA)._2()+(radius/2)>=objectsB.get(innerIndexB)._2()-(radius/2))){

                    if((Math.pow((objectsA.get(indexA)._2() - objectsB.get(innerIndexB)._2()), 2) + Math.pow((objectsA.get(indexA)._3()- objectsB.get(innerIndexB)._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(indexA)._3()-objectsB.get(innerIndexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA.get(indexA), objectsB.get(innerIndexB)));
                    }
                    innerIndexB++;
                }
                indexA++;
            }else{

                int innerIndexA = indexA;
                while(innerIndexA<objectsA.size() && (objectsB.get(indexB)._2()+(radius/2)>=objectsA.get(innerIndexA)._2()-(radius/2))){

                    if((Math.pow((objectsA.get(innerIndexA)._2() - objectsB.get(indexB)._2()), 2) + Math.pow((objectsA.get(innerIndexA)._3()- objectsB.get(indexB)._3()), 2) <= Math.pow(radius, 2))){
//                                 if(objectsA.get(innerIndexA)._3()-objectsB.get(indexB)._3()<=(radius/2)){
                        results.add(Tuple2.apply(objectsA.get(innerIndexA), objectsB.get(indexB)));
                    }
                    innerIndexA++;
                }
                indexB++;
            }
        }
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
        int indexA = 0;
        int indexB = 0;

        long counter = 0;

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

        int indexA = 0;
        int indexB = 0;
        long counter = 0;
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



}
