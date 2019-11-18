package edu.epsevg.prop.lab.c4;

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
    nom = "TuPutaMadre";
  }
  
  public int moviment(Tauler t, int color)
  {
    int col = (int)(t.getMida() * Math.random());
    while (!t.movpossible(col)) {
      col = (int)(t.getMida() * Math.random());
    }
    return col;
  }
  
  public int MinMax(int depth, Tauler t){
      return 0;
  }
  
  public int Max(int depth, Tauler t){
      //if(depth == 0)return heuristica(t);
      return 0;
  }
  
  public float heuristica(Tauler t, int color){
      float max = 0;
      for(int i = 0; i < t.getMida(); ++i){
          boolean adyacente = true;
          float v_pos = 0, v_col = 0;
          for(int j = 0; j < t.getMida() && adyacente; ++j){
             //Mira si esa posicion es adyacente ( al menos 1 de las posibles
             //posiciones es una ficha) y ademas no tiene una ficha en ese lugar
             if(t.getColor(i,j) == 0){
                 if(t.getColor(i-1,j) == 0){
                     v_pos = 0;
                     adyacente = false;
                 }
                 else{
                     //Cada una de estas funciones tendra que ponderar el valor retornado
                     //El valor sera negativo en caso de que la posicion fuese favorable para el oponente
                     if(t.getColor(i, j-1) != 0)v_pos += vertical(t,i,j,color);
                     if(t.getColor(i+1, j) != 0)v_pos += horizontalD(t,i,j,color);
                     if(t.getColor(i-1, j) != 0)v_pos += horizontalI(t,i,j,color);
                     if(t.getColor(i-1, j+1) != 0)v_pos += diagonalEsqSup(t,i,j,color);
                     if(t.getColor(i-1, j-1) != 0)v_pos += diagonalEsqInf(t,i,j,color);
                     if(t.getColor(i+1, j-1) != 0)v_pos += diagonalDchInf(t,i,j,color);
                     if(t.getColor(i+1, j+1) != 0)v_pos += diagonalDchSup(t,i,j,color);
                 }
             }else{
                 adyacente = false;
                 v_pos = 0;
             }
             v_col += v_pos;
          }
          //Valor de pos (v_pos) ya tiene que estar ponderado por la altura
          //respecto la ultima ficha a la cual se encuentra
          
          if(max < v_col)max = v_col;
      }
      return max;
  }
  
  public float vertical(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(alt > 0)color = t.getColor(col,alt-1);
      boolean cambiColor = false;
      for(int j = alt-1; j >= 0 && !cambiColor; --j){
          if(t.getColor(col,j) != color)cambiColor = true;
          else{
              if(color != mi_color)--valor;
              else ++valor;
          }
      }
      if (valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      return valor;
  }
  
  public float horizontalD(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(col < t.getMida()-1)color = t.getColor(col+1,alt);
      boolean cambiColor = false;
      for(int i = col+1; i < t.getMida() && !cambiColor; ++i){
          if(t.getColor(i,alt) != color)cambiColor = true;
          else{
              if(color != mi_color)--valor;
              else ++valor;
          }
      }
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      else valor = valor * espacioVacio(t, col, alt);
      
      return valor;
  }
  
  public float horizontalI(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(col > 0)color = t.getColor(col-1, alt);
      boolean cambiColor = false;
      for(int i = col-1; i >= 0 && !cambiColor; --i){
          if(t.getColor(i,alt) != color)cambiColor = true;
          else{
              if(color != mi_color)--valor;
              else ++valor;
          }
      }
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      else valor = valor * espacioVacio(t, col, alt);
      
      return valor;
  }
  
  public float diagonalDchSup(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(alt < t.getMida()-1 && col < t.getMida()-1)color = t.getColor(col+1,alt+1);
      boolean cambiColor = false;
      for(int i = col+1; i < t.getMida() && !cambiColor; ++i){
          for(int j = alt+1; j < t.getMida() && !cambiColor; ++j){
              if(t.getColor(i,j) != color)cambiColor = true;
              else{
                  if(color != mi_color)--valor;
                  else ++valor;
              }
          }
      }
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      else valor = valor * espacioVacio(t, col, alt);

      return valor;
  }
  
  public float diagonalEsqInf(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(alt > 0 && col > 0)color = t.getColor(col-1,alt-1);
      boolean cambiColor = false;
      for(int i = col-1; i >= 0 && !cambiColor; --i){
          for(int j = alt-1; j >= 0 && !cambiColor; --j){
              if(t.getColor(i,j) != color)cambiColor = true;
              else{
                  if(color != mi_color)--valor;
                  else ++valor;
              }
          }
      }
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      else valor = valor * espacioVacio(t, col, alt);
      
      return valor;
  }
  
  public float diagonalDchInf(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(alt > 0 && col < t.getMida()-1)color = t.getColor(col+1,alt-1);
      boolean cambiColor = false;
      for(int i = col+1; i < t.getMida() && !cambiColor; ++i){
          for(int j = alt-1; j >= 0 && !cambiColor; --j){
              if(t.getColor(i, j) != color)cambiColor = true;
              else{
                  if(color != mi_color)--valor;
                  else ++valor;
              }
          }
      }
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      else valor = valor * espacioVacio(t, col, alt);
      
      return valor;
  }
  
  
  public float diagonalEsqSup(Tauler t, int col, int alt, int mi_color){
      float valor = 0;
      int color = 0;
      if(alt > 0 && col < t.getMida()-1)color = t.getColor(col-1, alt+1);
      boolean cambiColor = false;
      for(int i = col-1; i >= 0 && !cambiColor; --i){
          for(int j = alt+1; j < t.getMida() && !cambiColor; ++j){
              if(t.getColor(i,j) != color)cambiColor = true;
              else{
                  if(color != mi_color)--valor;
                  else ++valor;
              }
          }
      }
      if(valor >= 3)valor = Float.POSITIVE_INFINITY;
      else if(valor <= -3)valor = Float.NEGATIVE_INFINITY;
      else valor = valor * espacioVacio(t, col, alt);
      
      return valor;
  }
  
  //Si la ficha inferior es suelo (fuera de las medidas del tablero) o
  //su color != 0, return 1, en otro caso, la distancia hasta una de estos dos
  //casos
  public int espacioVacio(Tauler t, int col, int alt){
      int vuit = 0;
      for(int i = alt; i >= 0; --i){
          if(t.getColor(col, i) == 0)++vuit;
      }
      return 1/vuit;
  }
  
  public String nom()
  {
    return nom;
  }
  
}
