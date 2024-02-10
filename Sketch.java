import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage; //import the PImage library

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.lang.reflect.Array;
import java.util.Random;

import javax.swing.text.html.HTMLDocument.Iterator;

import processing.core.PVector;


public class Sketch extends PApplet {

  float team1NestX = 100f;
  float team1NestY = 100f;

  float foodX = 700f;
  float foodY = 700f;

  int foodAmount = 5000;

  int team1NumOfAnts;
  int team2Number;

  int team1Food;
  int team2Food;

  int globalTime;
  int spawnRate;

  ArrayList<Ant> antsTeam1 = new ArrayList<Ant>();
  ArrayList<Ant> antsTeam2 = new ArrayList<Ant>();

  ArrayList<FoodTrail> foodTrails = new ArrayList<FoodTrail>();

  Random rand = new Random();

  public void settings() {
    size(800, 800);
  
  }

  public void setup() {
    
    background(255);
    
    team1NumOfAnts = 500;
    team1Food = 0;
    globalTime = 0;

    spawnRate = 10;

  }




  public void draw() {

    background(255);
    
    globalTime ++;

    // spawn ants
    try {
      if (globalTime % spawnRate == 0 && antsTeam1.size() < team1NumOfAnts) {
        antsTeam1.add(new Ant(team1NestX, team1NestY, rand.nextInt(11) > foodTrails.size(), foodTrails.get(rand.nextInt(foodTrails.size())), rand.nextBoolean()));
      }
    }
    catch (IllegalArgumentException e) {
      antsTeam1.add(new Ant(team1NestX, team1NestY, false, new FoodTrail(new ArrayList<PVector>()), rand.nextBoolean()));
    }

    // draw food trails
    if (foodTrails.size() > 0) {
      for (FoodTrail foodTrail : foodTrails) {

        for (int i = 0; i < foodTrail.trail.size() - 1; i++) {
          fill(200, 255, 200);;
          ellipse(foodTrail.trail.get(i).x, foodTrail.trail.get(i).y, 2, 2);
        }

        foodTrail.time ++;
        
        if (foodTrail.time > 1000) {
          foodTrails.remove(foodTrail);
        }
        }
      }
    
    
    // Delete food trails that are too long
    if (foodTrails.size() > 15) {
      
      int longestTrail = 0;

      for (int i = 0; i < foodTrails.size(); i++) {
        if (foodTrails.get(i).trail.size() > foodTrails.get(longestTrail).trail.size()) {
          longestTrail = i;
        }
      }
      
      for (Ant ant : antsTeam1) {
        if (ant.assignedTrail == foodTrails.get(longestTrail)) {

          ant.explorer = rand.nextInt(31) > foodTrails.size() * 2;

          if (ant.explorer) {
            ant.assignedTrail = new FoodTrail(new ArrayList<PVector>());
          }
          else {
            FoodTrail newAssignedTrail = foodTrails.get(rand.nextInt(foodTrails.size()));
            while (newAssignedTrail == foodTrails.get(longestTrail)) {
              newAssignedTrail = foodTrails.get(rand.nextInt(foodTrails.size()));
            }
            ant.assignedTrail = foodTrails.get(rand.nextInt(foodTrails.size()));
        }
      }
      }
      foodTrails.remove(longestTrail);
      foodTrails.trimToSize();
    }
    moveAnt();

    // draw food
    fill(0, 126, 0);
    ellipse(foodX, foodY, (int)(((double)foodAmount / 100)), (int)(((double)foodAmount / 100)));
    if (foodAmount <= 0) {
      foodX = 10000;
      foodY = 10000;
      foodAmount = 5000;
    }

    fill (255, 0, 0);
    ellipse(team1NestX, team1NestY, 25, 25);




    //debug
    fill(0);
    text("Number of Ants: " + team1NumOfAnts, 10, 10);
    text("Number of Food: " + team1Food, 10, 20);
    text("Global Time: " + globalTime, 10, 30);
    text("Number of Ants on Screen: " + antsTeam1.size(), 10, 40);
    text("Spawn Rate: " + spawnRate, 10, 50);
    text("Number of Food Trails: " + foodTrails.size(), 10, 60);
  }


  public void moveAnt() {


    for (Ant ant : antsTeam1) {
      
      if (!ant.goHome) {
        
        if (ant.explorer) {

          ant.x += Math.cos(ant.rotation) * 2;
          ant.y += Math.sin(ant.rotation) * 2;
          ant.rotation += rand.nextFloat() * 0.5 - 0.25;

          if (dist(ant.x, ant.y, foodX, foodY) <= (double)foodAmount / 60) {

            ant.rotation = (float) Math.atan2(foodY - ant.y, foodX - ant.x);
  
            if (dist(ant.x, ant.y, foodX, foodY) <= 10) {
              ant.goHome = true;
              ant.hasFood = true;
              ant.time = 0;
            }
          }

          if (ant.x > width) {
            ant.x = width;
            ant.rotation = (float) (Math.random() * Math.PI);
          }
          if (ant.x < 0) {
            ant.x = 0;
            ant.rotation = (float) (Math.random() * Math.PI);
          }
          if (ant.y > height) {
            ant.y = height;
            ant.rotation = (float) (Math.random() * Math.PI);
          }
          if (ant.y < 0) {
            ant.y = 0;
            ant.rotation = (float) (Math.random() * Math.PI);
          }
          if (ant.time > 750) {
            ant.goHome = true;
            if (ant.time > 2000) {
              ant.trail.clear();
              ant.time = 0;
              ant.x = team1NestX;
              ant.y = team1NestY;
              if (foodTrails.size() > 0) {
                ant.explorer = rand.nextInt(31) > foodTrails.size() * 2;
                ant.assignedTrail = foodTrails.get(rand.nextInt(foodTrails.size()));
              }
              else {
                ant.explorer = true;
              }
            } 
          }
        }
        
        else {

          try {
            if (dist(ant.x, ant.y, foodX, foodY) > (int)(((double)foodAmount / 50))) {
              if (ant.exploreUp) {
                ant.rotation = (float) Math.atan2(ant.assignedTrail.trail.get(ant.time).y 
                                - ant.y, ant.assignedTrail.trail.get(ant.time).x - ant.x)
                                + rand.nextFloat(0.7f);
              }
              else {
                ant.rotation = (float) Math.atan2(ant.assignedTrail.trail.get(ant.time).y 
                                - ant.y, ant.assignedTrail.trail.get(ant.time).x - ant.x)
                                - rand.nextFloat(0.7f);
              }
            }
            else if (dist(ant.x, ant.y, foodX, foodY) <= (int)(((double)foodAmount / 50))) {

              ant.rotation = (float) Math.atan2(foodY - ant.y, foodX - ant.x);
    
              if (dist(ant.x, ant.y, foodX, foodY) <= (int)(((double)foodAmount / 50))) {
                ant.goHome = true;
                ant.hasFood = true;
                ant.time = 0;
                foodAmount --;
              }
            }
            ant.x += Math.cos(ant.rotation) * 2;
            ant.y += Math.sin(ant.rotation) * 2;

          }
          catch (IndexOutOfBoundsException e) {

            ant.explorer = true;
            ant.assignedTrail = new FoodTrail(new ArrayList<PVector>());
            foodTrails.remove(ant.assignedTrail);
          }

          if (ant.time > 1000) {
            ant.goHome = true;

            if (ant.time > 2000) {
              ant.trail.clear();
              ant.time = 0;
              ant.x = team1NestX;
              ant.y = team1NestY;
              if (foodTrails.size() > 0) {
                ant.explorer = rand.nextInt(31) > foodTrails.size() * 2;
                ant.assignedTrail = foodTrails.get(rand.nextInt(foodTrails.size()));
              }
              else {
                ant.explorer = true;
              }
            } 
          }
        }

        ant.trail.add(new PVector(ant.x, ant.y));

      } 
      
      else {

        try {
          if (ant.hasFood) {
            ant.x = ant.trail.get(ant.trail.size() - ant.time - 1).x;
            ant.y = ant.trail.get(ant.trail.size() - ant.time - 1).y;
          }

          else {
            ant.x = ant.trail.get(ant.trail.size() - (ant.time - 500) - 1).x;
            ant.y = ant.trail.get(ant.trail.size() - (ant.time - 500) - 1).y;
          }

          if (dist(ant.x, ant.y, team1NestX, team1NestY) <= 50) {
            
            ant.rotation = atan2(team1NestY - ant.y, team1NestX - ant.x);
            ant.x += Math.cos(ant.rotation) * 2;
            ant.y += Math.sin(ant.rotation) * 2;
          }
        }
        catch (IndexOutOfBoundsException e) {

          if (ant.hasFood) {
            team1Food ++;
            ant.hasFood = false;
            ArrayList<PVector> temp = new ArrayList<PVector>(ant.trail);

            foodTrails.add(new FoodTrail(temp));
          }
          ant.goHome = false;
          ant.trail.clear();
          ant.time = 0;
          ant.x = team1NestX;
          ant.y = team1NestY;
          
          if (foodTrails.size() > 0) {
            ant.explorer = rand.nextInt(31) > foodTrails.size() * 2;
            ant.assignedTrail = foodTrails.get(rand.nextInt(foodTrails.size()));
          }
          else {
            ant.explorer = true;
          }
        }
      } 
      fill(0);
      noStroke();
      noTint();
      ellipse(ant.x, ant.y, 3, 3);

      ant.time ++;
    }
  } 
  public void keyPressed() {

    if (key == '=') {
      spawnRate++;
    }
    else if (key == '-') {
      spawnRate --;
    }

    if (key == 'r') {
      foodX = rand.nextInt(width);
      foodY = rand.nextInt(height);
      foodAmount = 5000;
    }
  }
}