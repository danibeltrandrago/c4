package edu.epsevg.prop.lab.c4;
import java.util.LinkedList;

/**
 * Jugador aleatori
 * "Alea jacta est"
 * @author Profe
 */
public class MinMax_1
  implements Jugador, IAuto
{
  private String nom;
  private int my_color;
  
  public MinMax_1()
  {
    nom = "TuPutaMadre";
  }
  
  public int moviment(Tauler t, int color)
  {
    my_color = color;
    int col = (int)(t.getMida() * Math.random());
    while (!t.movpossible(col)) {
      col = (int)(t.getMida() * Math.random());
    }
    heuristica(t, color);
    return col;                 
  }
  
  public float MinMax(Tauler t, int depth, boolean minmax,int color){
      LinkedList <Tauler> valid_locations = get_valid_locations(t);
      float valor = heuristica(t,my_color);
      boolean is_terminal = valor == Float.POSITIVE_INFINITY || valor == Float.NEGATIVE_INFINITY || !t.espotmoure();
      if (depth == 0 || is_terminal) {
          int posible = t.espotmoure() ? 1 : 0;
          return valor * posible;
      }                
              
      if (minmax){
          valor = Float.NEGATIVE_INFINITY;
          //per cada columna
              
          
      } else {
          valor = Float.POSITIVE_INFINITY;
      }
     
  }
  
  public LinkedList<Tauler> get_valid_locations(Tauler t){
      LinkedList<Tauler> valid_loc = new LinkedList<>();
      for(int i = 0; i < t.getMida(); i++){
          if(t.movpossible(i)){
              t.afegeix(i, my_color);
              valid_loc.add(t);
          }
      }
      return valid_loc;
  }
    
  public float heuristica(Tauler t, int color){
      float max = 0;
      for(int j = 0; j < t.getMida(); j++){
          for(int i = t.getMida()-1; i > -1 && t.getColor(t.getMida()-1,j) == 0; i--){
                  if(t.getColor(i,j) == 0){
                      if(i>0)if(t.getColor(i-1,j) != 0){
                          max+=vertical(t, j, i-1, color);
                      }
                      if(j>0)if(t.getColor(i, j-1) != 0){
                          max+=horizontalI(t,j-1,i,color);
                      }
                      if(j<t.getMida()-1)if(t.getColor(i, j+1) != 0){
                          max+=horizontalD(t,j+1,i,color);
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
          if(color != t.getColor(i,col))trobat = true; //#################################################### color -> mi_color
          else ++valor;
      }
      
      valor = valor * espacioVacio(t, col, alt+1); // dudaaaaaaaaaa??
      if(valor >= 3)valor = Float.POSITIVE_INFINITY; // si la distancia a una ficha de las mias es mayor k 3 valor es inf
      
      if(color != mi_color && valor != 0)return -valor;
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
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
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
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
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
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
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
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
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
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
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
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      
      if(color != mi_color && valor != 0)return -valor;
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
      return 1/vuit;
  }
      
  public String nom()
  {
    return nom;
  }
  
}
