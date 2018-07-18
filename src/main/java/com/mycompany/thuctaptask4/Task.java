/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thuctaptask4;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.graph.Graph;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 *
 * @author Xuan Cuong
 */
 /* Chinh sua tren server may client*/
public class Task {
    

    public static boolean test(int n) {
        int cout = 0;
        if (n < 2) {
            return false;
        } 
        else {
            for (int i = 2; i < n / 2; i++) {
                if (n % i == 0) {
                    cout++;
                }
            }
            if (cout == 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static ArrayList<Integer> primes(int n) {
        ArrayList<Integer> pri = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (test(i)) {
                pri.add(i);
            }
        }
        return pri;
    }

    public static void main(String[] args) {
        LoadingCache<Integer, ArrayList> cache;
                {
		cache = CacheBuilder.newBuilder()
		       .maximumSize(100) //set size
		       .expireAfterWrite(20, TimeUnit.SECONDS) //set time expire
                       .expireAfterAccess(10, TimeUnit.SECONDS)
		       .build(
		           new CacheLoader<Integer, ArrayList>() {
						@Override
						public ArrayList load(Integer id) throws Exception {
							return primes(id);
						}
		           }
		       );
                }
        Spark.get("/prime", (rqst, rspns) -> {
           
            return cache.get(Integer.parseInt(rqst.queryParams("n")));
           
           
        });
        Spark.get("/hello", new Route() {

            @Override
            public Object handle(Request arg0, Response arg1) throws Exception {
                return "Hello World from Spark";
            }

        });
         
    }}