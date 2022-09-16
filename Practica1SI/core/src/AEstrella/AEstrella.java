/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AEstrella;

import java.util.ArrayList;

/**
 *
 * @author mirse
 */
public class AEstrella {
    
    //Mundo sobre el que se debe calcular A*
    Mundo mundo;
    
    //Camino
    public char camino[][];
    
    //Casillas expandidas
    int camino_expandido[][];
    
    //Número de nodos expandidos
    int expandidos;
    
    //Coste del camino
    float coste_total;
        
    public AEstrella(){
        expandidos = 0;
        mundo = new Mundo();
    }
    
    public AEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    
    
    public Nodo nodoOptimoOpenSet(ArrayList<Nodo> openSet){
        int indexGanador = 0;
        for(int i = 1; i < openSet.size(); i++){
            if(openSet.get(i).getF() < openSet.get(indexGanador).getF())
                indexGanador = i;
        }
        return openSet.get(indexGanador);
    }
    
    public int estaEnOCSet(ArrayList<Nodo> OCSet, Nodo buscado){
        int encontrado = -1;
        
        for(int i = 0; i < OCSet.size() && encontrado == -1; i++){
            if(OCSet.get(i).getCoord().getX() == buscado.getCoord().getX()
               && OCSet.get(i).getCoord().getY() == buscado.getCoord().getY())
                encontrado = i;
        }
        
        return encontrado;
    }
    
    int calcularHeuristica(Nodo nodo1, Nodo nodo2){
        /*
        //Heurística sin definir
        return 0;
        */
        
        /*
        //Distancia Manhattan
        return (Math.abs(nodo2.getX() - nodo1.getX()) + Math.abs(nodo2.getY() - nodo1.getY()));
        */
        
        /*
        //Distancia euclídea
        return (int) (Math.sqrt(
                    Math.pow(nodo2.getX() - nodo1.getX(), 2) 
                    + Math.pow(nodo2.getY() - nodo1.getY(), 2)
                )
        );
        */
    
        
        //Heurística basada en sistema de referencia hexagonal
        int nodo1CoordZ= - nodo1.getX() - nodo1.getY();
        int nodo2CoordZ= - nodo2.getX() - nodo2.getY();
        
        return (Math.abs(nodo1.getX() - nodo2.getX())
                + Math.abs(nodo1.getY() - nodo2.getY())
                + Math.abs(nodo1CoordZ - nodo2CoordZ))/2;
        
    }
    
    public void reconstruirCamino(char [][] camino, Nodo ganador){
        if(ganador.getPadre() != null){
            camino[ganador.getY()][ganador.getX()] = 'X';
            //System.out.println(ganador.toString());
            reconstruirCamino(camino, ganador.getPadre());
        }
    }
    
    //Calcula el A*
    public int CalcularAEstrella(){
        long tiempoInicio = System.currentTimeMillis();
        boolean encontrado = false;
        int result = -1;
        
        ArrayList <Nodo> openSet = new ArrayList(); //Lista frontera
        ArrayList <Nodo> closeSet = new ArrayList();//Lista interior  

        Nodo inicio = new Nodo(mundo.caballero);
        Nodo meta = new Nodo(mundo.dragon);
                
        openSet.add(inicio);
        int orden = 0;
        
        while(!openSet.isEmpty()){
            
            Nodo ganador = nodoOptimoOpenSet(openSet);
            camino_expandido[ganador.getY()][ganador.getX()] = orden;
            System.out.println("El ganador es:" + ganador.toString());
            orden++;
            
            if(ganador.equals(meta)){
                result = 0;
                reconstruirCamino(camino, ganador);
                coste_total = ganador.getF();
                encontrado = true;
                break;   
            }
            
            else{             
                openSet.remove(openSet.indexOf(ganador));
                closeSet.add(ganador);
                
                //Recorremos ls vecinos del ganador
                for(Nodo vecino : ganador.encontrarVecinas()){       
                    //... si el vecino no esta en closeSet
                    if(estaEnOCSet(closeSet, vecino) == -1){
                        expandidos++;
                        
                        //obtenemos el peso de llegar a él
                        int gTemporal = ganador.getG() + vecino.coste;
                        
                        //si estaba ya en openSet, pero esta ruta es mejor la modificamos
                        if(estaEnOCSet(openSet, vecino) != -1){
                            Nodo vecinoModificado = openSet.get(estaEnOCSet(openSet, vecino));
                            if(vecinoModificado.getG() > gTemporal){
                               vecinoModificado.setG(gTemporal);
                               vecinoModificado.setH(calcularHeuristica(vecino, meta));
                               vecinoModificado.setF(vecinoModificado.getG() + vecinoModificado.getH());
                               vecinoModificado.setPadre(ganador); 
                            }   
                        }
                        else{
                            //sino estaba en openSet tenemos que añadir el nodo a la lista
                            vecino.setG(gTemporal);
                            vecino.setH(calcularHeuristica(vecino, meta)); 
                            vecino.setF(vecino.getG() + vecino.getH());
                            
                            vecino.setPadre(ganador);
                            openSet.add(vecino);
                        }   
                    }
                }
            }
            /*
            System.out.println("OpenSet");
            for(Nodo n:openSet){System.out.println(n.toString());}
            System.out.println("CloseSet");
            for(Nodo n:closeSet){System.out.println(n.toString());}
            */
        }
        
        //-----------------------------------------------------------------------
        //Si ha encontrado la solución, es decir, el camino, muestra las matrices camino y camino_expandidos y el número de nodos expandidos
        if(encontrado){
            
            openSet.clear();
            closeSet.clear();
            //Mostrar las soluciones
            System.out.println("Camino");
            mostrarCamino();

            System.out.println("Camino explorado");
            mostrarCaminoExpandido();
            
            System.out.println("Nodos expandidos: "+expandidos);
        }
        
        long tiempoFinal = System.currentTimeMillis();
        double tiempoTotal = (double) ((tiempoFinal - tiempoInicio));
        System.out.println("El tiempo invertido en la ejecución ha sido de: " + tiempoTotal + " ms.");
        return result;
    }
    
    //Muestra la matriz que contendrá el camino después de calcular A*
    public void mostrarCamino(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                System.out.print(camino[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    //Muestra la matriz que contendrá el orden de los nodos expandidos después de calcular A*
    public void mostrarCaminoExpandido(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                    System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                if(camino_expandido[i][j]>-1 && camino_expandido[i][j]<10)
                    System.out.print(" ");
                System.out.print(camino_expandido[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    public void reiniciarAEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    public float getCosteTotal(){
        return coste_total;
    } 
    
    class Nodo{
        Nodo padre;
        Coordenada localizacion;
        int coste;
        int f;
        int g;
        int h;
        
        Nodo(Coordenada c){
            this.padre = null;
            this.f = this.g = this.h = 0;
            
            this.localizacion = new Coordenada(c);
            switch(mundo.getCelda(this.localizacion.getX(), this.localizacion.getY())){
                case 'c':
                    coste = 1;
                break;
                
                case 'd':
                    coste = 1;
                break;
                
                case 'h':
                    coste = 2;
                break;
                
                case 'a':
                    coste = 3;
                break;     
            }
        } 
        
        Coordenada getCoord() {return this.localizacion;}
        int getX(){return this.localizacion.getX();}
        int getY(){return this.localizacion.getY();}
        int getF(){return this.f;}
        int getG(){return this.g;}
        int getH(){return this.h;}
        Nodo getPadre(){return this.padre;}
        char getMaterial(){return mundo.getCelda(localizacion.getX(), localizacion.getY());}

        void setF(int f){this.f = f;}
        void setG(int g){this.g = g;}
        void setH(int h){this.h = h;}
        void setPadre(Nodo n){this.padre = n;}
        
        public ArrayList<Nodo> encontrarVecinas(){
             ArrayList<Nodo> vecinas = new ArrayList<>();

             //Recorremos las 9 posiciones que hay contiguas en la matriz, incluyendose a si misma
             for(int dx = this.getX() - 1; dx <= this.getX() + 1; dx++){
                 for(int dy = this.getY() - 1; dy <= this.getY() + 1; dy++){

                     Nodo posibleVecina = new Nodo(new Coordenada(dx, dy));
                     //Si es un bloque excluida directamente
                     if(posibleVecina.getMaterial() != 'b' && posibleVecina.getMaterial() != 'p'){
                         //Eliminamos la propia casilla y las que no son adyacentes en cada caso...
                         if(!(posibleVecina.getX() == this.getX() && posibleVecina.getY() == this.getY())){
                             //.. si la casilla es fila par las invalidas son (-1,-1) y (+1, -1)
                             if(this.getY() % 2 == 0 ){

                                if(posibleVecina.getX() == this.getX()+1 || posibleVecina.getX() == this.getX() ||
                                   ((posibleVecina.getX() == this.getX() - 1) && (posibleVecina).getY() == this.getY()))
                                    vecinas.add(posibleVecina);
                             }
                             else{
                                 if( (posibleVecina.getX() == this.getX() - 1) || (posibleVecina.getX() == this.getX()) ||
                                     ((posibleVecina.getX() == this.getX() + 1) && (posibleVecina).getY() == this.getY()))
                                     vecinas.add(posibleVecina);
                                     //System.out.println("Coordenada apta");
                                 //else System.out.println("Excluida por no adyacente");
                             }
                         }
                     }
                     //else System.out.println("Excluida por obstaculo");
                 }
             }
             return vecinas;
         }

        @Override
        public String toString (){
            return "Coord:(" + localizacion.getX() + "," 
                             + localizacion.getY() + ") F[" 
                             + this.f + "] G[" 
                             + this.g + "] H[" + this.h + "]" ;
        }

        public boolean equals(Nodo n) {
            return n.getX() == this.getX() && n.getY() == this.getY();                     
        } 
    }   
}