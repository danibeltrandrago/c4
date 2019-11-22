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
  private int my_color = 1;
  private boolean sol = false;
  private int col = -1;
  
  public MinMax_1()
  {
    nom = "TuPutaMadre";
  }
  
  public int moviment(Tauler t, int color)
  {
    my_color = color;
    sol = false;
    for(int i = 0; i < t.getMida() && t.espotmoure(); i++){
        
    }
    return MinMax(t,8,true, -9999999, 9999999).getKey();
  }
  
  public Pair <Integer,Float> MinMax(Tauler t, int depth, boolean minmax, float alpha, float beta){
    if (depth == 0 || !t.espotmoure()) {
        for(int i = 0; i < t.getMida(); i++){
            if(t.solucio(i, 1))return new Pair<>(i,Float.MAX_VALUE);
            else if(t.solucio(i, -1))return new Pair<>(i,Float.MIN_VALUE);
            else return new Pair<>(i,(float)(0));
        }
    }                
    if (minmax){
        float valor = -9999999;
        int column = 0;
        boolean guanyador = false;
        for (int i = 0; i < t.getMida() && !guanyador;i++){
            if(t.movpossible(i)){
                Tauler taux = new Tauler(t);
                taux.afegeix(i, -1);
                if(taux.solucio(i, -1) == true){
                    System.out.println("Solucio neg");
                    return new Pair<>(i,Float.MIN_VALUE);
                }
                float nouValor = MinMax(taux,depth-1,false,  alpha, beta).getValue();
                if (nouValor > valor){
                    valor = nouValor;
                    column = i;
                }
                alpha = Math.max(alpha, valor);
                if(alpha >= beta){
                    return new Pair<>(i, alpha);
                }
                
            }
        }
        return new Pair<>(column, valor);
    } else {
        float valor = 9999999;
        int column = 0;
        boolean guanyador = false;
        for (int i = 0; i < t.getMida() && !guanyador; i++){
            if(t.movpossible(i)){
                Tauler taux = new Tauler(t);
                taux.afegeix(i, my_color);
                if(taux.solucio(i, my_color) == true){
                    System.out.println("Solucio Pos");
                    return new Pair<>(i,Float.MAX_VALUE);
                }else{
                    float nouValor = MinMax(taux,depth-1,true, alpha, beta).getValue();
                    if (nouValor < valor){
                        valor = nouValor;
                        column = i;
                    }
                    beta = Math.min(beta, valor);
                    if(alpha >= beta){
                        return new Pair<>(i, beta);
                    }
                }
            }
        }
        return new Pair<>(column, valor);
    }
  }
  
  public float heuristica(Tauler t, int color){
      float max = 0;
      for(int j = 0; j < t.getMida(); j++){
          for(int i = t.getMida()-1; i > -1 && t.movpossible(i); i--){
                  if(t.getColor(i,j) == 0){
                      if(i>0)if(t.getColor(i-1,j) != 0){
                          max+=2*vertical(t, j, i-1, color);
                          //System.out.print(" V["+i+","+j+"]:"+vertical(t, j, i-1, color));
                      }
                      if(j>0)if(t.getColor(i, j-1) != 0){
                          max+=2*horizontalI(t,j-1,i,color);
                          //System.out.print(" Hi["+i+","+j+"]:"+horizontalI(t,j-1,i,color));
                      }
                      if(j<t.getMida()-1)if(t.getColor(i, j+1) != 0){
                          max+=2*horizontalD(t,j+1,i,color);
                          //System.out.print(" Hd["+i+","+j+"]:"+horizontalD(t,j+1,i,color));
                      }
                      if(j<t.getMida()-1 && i>0)if(t.getColor(i-1,j+1) != 0){
                          max+=diagonalDchInf(t, j+1, i-1, color);
                          //System.out.print(" Ddi["+i+","+j+"]:"+diagonalDchInf(t, j+1, i-1, color));
                      }
                      if(j<t.getMida()-1 && i<t.getMida()-1)if(t.getColor(i+1,j+1) != 0){
                          max+=diagonalDchSup(t, j+1, i+1, color);
                          //System.out.print(" Dds["+i+","+j+"]:"+diagonalDchSup(t, j+1, i+1, color));                          
                      }
                      if(j>0 && i>0)if(t.getColor(i-1,j-1) != 0){
                          max+=diagonalEsqInf(t, j-1, i-1, color);
                          //System.out.print(" Dei["+i+","+j+"]:"+diagonalEsqInf(t, j-1, i-1, color));
                      }
                      if(j>0 && i>t.getMida()-1)if(t.getColor(i+1,j-1) != 0){
                          max+=diagonalEsqSup(t, j-1, i+1, color);
                          //System.out.print(" Des["+i+","+j+"]:"+diagonalEsqSup(t, j-1, i+1, color));
                      }
                  }
          }
      }
      //System.out.println("");
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
      
      if(valor >= 3)valor = Float.POSITIVE_INFINITY; // si la distancia a una ficha de las mias es mayor k 3 valor es inf
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