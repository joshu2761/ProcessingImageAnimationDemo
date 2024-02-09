import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage; //import the PImage library

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.lang.reflect.Array;
import java.util.Random;
import processing.core.PVector;


public class Sketch extends PApplet {

  float team1NestX = 100f;
  float team1NestY = 100f;

  float foodX = 500f;
  float foodY = 500f;

  int team1NumOfAnts;
  int team2Number;

  int team1Food;
  int team2Food;

  int globalTime;
  int spawnRate;

  ArrayList<Ant> antsTeam1 = new ArrayList<Ant>();
  ArrayList<Ant> antsTeam2 = new ArrayList<Ant>();

  ArrayList<ArrayList> foodTrails = new ArrayList<ArrayList>();

  Random rand = new Random();

  public void settings() {
    size(600, 600);
  
  }

  public void setup() {
    
    background(255);
    
    antsTeam1.add(new Ant(team1NestX, team1NestY));
    team1NumOfAnts = 100;
    team1Food = 0;
    globalTime = 0;

    spawnRate = 10;

}

  public void draw() {

    background(255);
    moveAnt();
    
    globalTime ++;
    if (globalTime % spawnRate == 0 && antsTeam1.size() < team1NumOfAnts){
      antsTeam1.add(new Ant(team1NestX, team1NestY));
    }

    fill (255, 0, 0);
    ellipse(team1NestX, team1NestY, 25, 25);

    fill(0, 255, 0);
    ellipse(foodX, foodY, 25, 25);

    if (foodTrails.size() > 0) {
      for (ArrayList<PVector> trail : foodTrails) {
        for (int i = 0; i < trail.size() - 1; i++) {
          fill(0, 255, 0);
          tint(255, 126);
          ellipse(trail.get(i).x, trail.get(i).y, 2, 2);
        }
      }
    }

    if (foodTrails.size() > 10) {
      
      int longestTrail = 0;

      for (int i = 0; i < foodTrails.size(); i++) {
        if (foodTrails.get(i).size() > foodTrails.get(longestTrail).size()) {
          longestTrail = i;
        }
      }

      foodTrails.remove(longestTrail);
    }

    //debug
    fill(0);
    text("Number of Ants: " + team1NumOfAnts, 10, 10);
    text("Number of Food: " + team1Food, 10, 20);
    text("Global Time: " + globalTime, 10, 30);
    text("Number of Ants on Screen: " + antsTeam1.size(), 10, 40);
    text("Spawn Rate: " + spawnRate, 10, 50);
  }

  public void moveAnt() {

    for (Ant ant : antsTeam1) {
      
      if (!ant.goHome) {
        
        if (rand.nextInt(11) > foodTrails.size()) {

          ant.x += Math.cos(ant.rotation) * 2;
          ant.y += Math.sin(ant.rotation) * 2;
          ant.rotation += rand.nextFloat() * 0.5 - 0.25;
  
          if (ant.x < 0 || ant.x > width || ant.y < 0 || ant.y > height) {
            ant.rotation = -ant.rotation;
          }
          if (ant.time > 500) {
            ant.goHome = true;
          }
          
        }
        


        if (dist(ant.x, ant.y, foodX, foodY) < 50) {

          ant.rotation = (float) Math.atan2(foodY - ant.y, foodX - ant.x);

          if (dist(ant.x, ant.y, foodX, foodY) < 25) {
            ant.goHome = true;
            ant.hasFood = true;
            ant.time = 0;
          }
        }

        ant.trail.add(new PVector(ant.x, ant.y));

      } 
      
      else {

        try {
          if (ant.hasFood) {
            ant.x = ant.trail.get(ant.trail.size() - ant.time - 1).x;
            ant.y = ant.trail.get(ant.trail.size() - ant.time - 1).y;

            for (int i = ant.trail.size() - 1; i >= ant.trail.size() - ant.time; i--) {
              
              fill(0, 255, 0);
              tint(255, 126);
              ellipse(ant.trail.get(i).x, ant.trail.get(i).y, 2, 2);
            }
          }

          else {
            ant.x = ant.trail.get(ant.trail.size() - (ant.time - 500) - 1).x;
            ant.y = ant.trail.get(ant.trail.size() - (ant.time - 500) - 1).y;
          }
        }
        catch (IndexOutOfBoundsException e) {

          if (ant.hasFood) {
            team1NumOfAnts ++;
            team1Food += 10;
            ant.hasFood = false;
            ArrayList<PVector> temp = new ArrayList<PVector>(ant.trail);
            foodTrails.add(temp);
          }
          ant.goHome = false;
          ant.trail.clear();
          ant.time = 0;
        }
    }
      fill(0);
      noStroke();
      noTint();
      ellipse(ant.x, ant.y, 2, 2);

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
  }
}


