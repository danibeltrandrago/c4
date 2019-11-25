package edu.epsevg.prop.lab.c4;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author Profe
 */
public class MinMax_1
  implements Jugador, IAuto
{
  private String nom;
  
  public MinMax_1()
  {
    nom = "Danésar";
  }
  
  public int moviment(Tauler t, int color)
  {
    int col = t.getMida()/2;
    float alpha = Float.NEGATIVE_INFINITY;
    for(int i = 0; i < t.getMida() && t.espotmoure(); i++){
        Tauler aux = new Tauler(t);
          if(aux.movpossible(i)){
              aux.afegeix(i, 1);
              if(aux.solucio(i, 1))return i;
              else{
                  float nvalor = Min(aux,1,alpha,Float.POSITIVE_INFINITY,7);
                  if(nvalor > alpha)col = i;
                  alpha = Math.max(alpha,nvalor);
              }
          }
    }
    
    int i = 0;
    while (!t.movpossible(col) && i < t.getMida()){
        col = i;
        i++;
    }
    return col;
  }
  
  public float Max(Tauler t, int color, float alpha, float beta, int depth){
      if(!t.espotmoure() || depth == 0){
          return basicHeuristica(t);
      }
      for(int i = 0; i < t.getMida(); i++){
          Tauler aux = new Tauler(t);
          if(aux.movpossible(i)){
              aux.afegeix(i, 1);
              if(aux.solucio(i, 1))return Float.POSITIVE_INFINITY;
              else{
                  float nvalor = Min(aux,1,alpha,beta,depth-1);
                  alpha = Math.max(alpha,nvalor);
                  if(beta <= alpha)return beta;
              }
          }
      }
      return alpha;
  }
  
  public float Min(Tauler t, int color, float alpha, float beta, int depth){
      if(!t.espotmoure() || depth == 0){
          return heuristica(t, 1);
      }
      for(int i = 0; i < t.getMida(); i++){
          Tauler aux = new Tauler(t);
          if(aux.movpossible(i)){
              aux.afegeix(i, -1);
              if(aux.solucio(i, -1))return Float.NEGATIVE_INFINITY;
              else{
                  float nvalor = Max(aux,-1,alpha,beta,depth-1);
                  beta = Math.min(beta, nvalor);
                  if(beta <= alpha)return alpha;  
              }          
          }
      }
      return beta;
  }
  
  public float basicHeuristica(Tauler t){
      if(t.espotmoure() == false)return Float.NEGATIVE_INFINITY;
      else{
          for(int i = 0; i < t.getMida(); i++){
              if(t.solucio(i, 1)){
                  System.out.println("Solucio+");
                  return Float.POSITIVE_INFINITY;
              }
              else if(t.solucio(i, -1)){
                  System.out.println("Solucio-");
                  return Float.NEGATIVE_INFINITY;
              }
          }
          return (float) 0;
      }
  }
  
  
  public float heuristica(Tauler t, int color){
      float max = 0;
      for(int j = 0; j < t.getMida(); j++){
          for(int i = t.getMida()-1; i > -1 && t.movpossible(i); i--){
                  if(t.getColor(i,j) == 0){
                      if(i>0)if(t.getColor(i-1,j) != 0){
                          max+=2*vertical(t, j, i-1, color);
                      }
                      if(j>0)if(t.getColor(i, j-1) != 0){
                          max+=2*horizontalI(t,j-1,i,color);
                      }
                      if(j<t.getMida()-1)if(t.getColor(i, j+1) != 0){
                          max+=2*horizontalD(t,j+1,i,color);
                      }
                      if(j<t.getMida()-1 && i>0)if(t.getColor(i-1,j+1) != 0){
                          max+=diagonalDchInf(t, j+1, i-1, color);
                      }
                      if(j<t.getMida()-1 && i<t.getMida()-1)if(t.getColor(i+1,j+1) != 0){
                          max+=diagonalDchSup(t, j+1, i+1, color);                        
                      }
                      if(j>0 && i>0)if(t.getColor(i-1,j-1) != 0){
                          max+=diagonalEsqInf(t, j-1, i-1, color);
                      }
                      if(j>0 && i>t.getMida()-1)if(t.getColor(i+1,j-1) != 0){
                          max+=diagonalEsqSup(t, j-1, i+1, color);
                      }
                  }
          }
      }
      return max;
  }
  
  public float vertical(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = alt; i >= 0 && !trobat; i--){
          if(color != t.getColor(i,col))trobat = true;
          else ++valor;
      }
      
      if(valor >= 3)valor = Float.POSITIVE_INFINITY; 
      else Math.pow(4,valor);
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  public float horizontalI(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i >= 0 && !trobat; i--){
          if(color != t.getColor(alt,i))trobat = true;
          else ++valor;
      }
  
      valor = valor * espacioVacio(t, col+1, alt);
      if(valor > 2)valor = Float.POSITIVE_INFINITY;
      else Math.pow(4,valor);
      
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  public float horizontalD(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i < t.getMida() && !trobat; i++){
          if(color != t.getColor(alt,i))trobat = true;
          else ++valor;
      }
      
      valor = valor * espacioVacio(t, col-1, alt);
      if(valor > 2)valor = Float.POSITIVE_INFINITY;
      else Math.pow(4,valor);
      
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalDchInf(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i < t.getMida() && !trobat; i++){
          for(int j = alt; j >= 0 && !trobat; j--){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col-1, alt+1);
      if(valor >= 4)valor = Float.POSITIVE_INFINITY;
      else Math.pow(4,valor);
      
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalDchSup(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i < t.getMida() && !trobat; i++){
          for(int j = alt; j < t.getMida() && !trobat; j++){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col-1, alt-1);
      if(valor >= 4)valor = Float.POSITIVE_INFINITY;
      else Math.pow(4,valor);
      
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalEsqInf(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i >= 0 && !trobat; i--){
          for(int j = alt; j >= 0 && !trobat; j--){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col+1, alt+1);
      if(valor >= 4)valor = Float.POSITIVE_INFINITY;
      else Math.pow(4,valor);
      
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  public float diagonalEsqSup(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = t.getColor(alt,col);
      boolean trobat = false;
      for(int i = col; i >= 0 && !trobat; i--){
          for(int j = alt; j < t.getMida() && !trobat; j++){
              if(color != t.getColor(j,i))trobat = true;
              else valor++;
          }
      }
      
      valor = valor * espacioVacio(t, col+1, alt-1);
      if(valor >= 4)valor = Float.POSITIVE_INFINITY;
      else Math.pow(4,valor);
      
      if(color == -1 && valor != 0)return -valor;
      else return valor;
  }
  
  //Si la ficha inferior es suelo (fuera de las medidas del tablero) o
  //su color != 0, return 1, en otro caso, la distancia hasta una de estos dos
  //casos
  public float espacioVacio(Tauler t, int col, int alt){
      float vuit = 0;
      boolean trobat = false;
      for(int i = alt; i >= 0 && !trobat; i--){
          if(t.getColor(col,i) == 0)++vuit;
          else trobat = true;
      }
      if(vuit == 0)return 1;
      else return 1/vuit;
  }
    
  public String nom()
  {
    return nom;
  }
  
}