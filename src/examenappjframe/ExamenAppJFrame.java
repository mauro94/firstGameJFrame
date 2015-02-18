/**
 * Juego
 *
 * Juego en el que la changuita Nena debera de eliminar a los fantasmas 
 * y evitar a todos los juanitos!
 *
 * @author Mauro Amarante and
 * @version 2.0
 * @date 2015/2/11
 */

package examenappjframe;

import javax.swing.JFrame;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;

public class ExamenAppJFrame extends JFrame implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basNena;         // Objeto de la clase base
    private LinkedList<Base> lklJuanitos;    //Lista de objetos de la clase Base
    private LinkedList<Base> lklFantasmas;    //Lista de objetos de la clase Base
    private int iVidas;     //numero de vidas
    private int iPuntos;    //numero de puntos acumulados
    private int iDireccion; //define la direccion del movimiento
    private int iContadorJuanitos;  //cuenta la colision de juanitos con Nena
    private boolean bPausa; //pausa para el juego
    private boolean bEscape;    //escape termina el juego
    private static final int WIDTH = 800;    //Ancho del JFrame
    private static final int HEIGHT = 500;    //Alto del JFrame
    private Image imaImagenGameOver;   // Imagen game over
    private String nombreArchivo;    //Nombre del archivo.
    private String[] arr;    //Arreglo del archivo divido.
    
    
    /* objetos para manejar el buffer del Jframe y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en JFrame
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private SoundClip adcSonidoJuanito;   // Objeto sonido de Juanito
    private SoundClip adcSonidoFantasma;   // Objeto sonido de Fantasma
    
    /** 
     * ExamenAppJFrame
     * 
     * Constructor de la clase <code>ExamenAppJFrame</code>.<P>
     * En este metodo se crea una instancia de la clase 
     * <code>ExamenAppJFrame</code>
     * 
     */
    public ExamenAppJFrame() {        
        //Se define el nuero de vidas entre 3 y 5 de la changuita
        iVidas = (int) (Math.random() * 3) + 3;
        
        //Se inician los puntos en 0
        iPuntos = 0;
        
        //se inicia el movimiento en ninguna direccion (inexistente)
        iDireccion = -1;
        
        //se inicia contador en 0
        iContadorJuanitos = 0;
        
        //se inicia sin pausa el juego
        bPausa = false;
        
        //se inicia si terminar el juego
        bEscape = false;
        
        //nombrar archivo 
        nombreArchivo = "SaveJuego.txt";
        
        //definir imagen de game over
        URL urlImagenGameOver = this.getClass().getResource("gameOver.gif");
        imaImagenGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);
        
        // se posiciona Nena
 	int iPosX = (int) (Math.random() *(WIDTH / 4)) + WIDTH / 2;    
        int iPosY = (int) (Math.random() *(HEIGHT / 4)) + HEIGHT / 2;
        
        //Definir imagenen para Nena
	URL urlImagenNena = this.getClass().getResource("chimpy.gif");
                
        //se crea el objeto para Nena
	basNena = new Base(iPosX, iPosY, WIDTH / iMAXANCHO,
              	  HEIGHT / iMAXALTO,
                	Toolkit.getDefaultToolkit().getImage(urlImagenNena));
        
        //se reposiciona a Nena  en el piso del Applet y al centro
        basNena.setX(WIDTH / 2 - basNena.getAncho() / 2);
        basNena.setY(HEIGHT / 2 - basNena.getAlto() / 2);
        
        //defino la imagen de los Juanitos
        URL urlImagenJuanito = this.getClass().getResource("juanito.gif");
        
        // se posiciona a Juanito 
        iPosX = (iMAXANCHO - 1) * WIDTH / iMAXANCHO;
        iPosY = (iMAXALTO - 1) * HEIGHT / iMAXALTO;    
        
        //se crea la lista de juanitos
        lklJuanitos = new LinkedList();
        
        // genero un numero azar de 10 a 15
        int iAzar = (int) (Math.random() * 6) + 10;
       
        // genero cada juanito y lo añado a la lista
        for (int iI = 0; iI < iAzar; iI ++) {
            // se crea el objeto para juanito
            Base basJuanito = new Base(iPosX,iPosY, WIDTH / iMAXANCHO,
                        HEIGHT / iMAXALTO,
                        Toolkit.getDefaultToolkit().getImage(urlImagenJuanito));
            basJuanito.setX((int) (Math.random() * 
                                    (WIDTH - basJuanito.getAncho())));   
            basJuanito.setY(-basJuanito.getAlto() + 
                                -((int) (Math.random() * HEIGHT)));
            lklJuanitos.add(basJuanito);
        }
        
        // defino la imagen de los fantasmas
	URL urlImagenFantasma = this.getClass().getResource("fantasmita.gif");
        
        // se posiciona a fantasma 
        iPosX = (iMAXANCHO - 1) * WIDTH / iMAXANCHO;
        iPosY = (iMAXALTO - 1) * HEIGHT / iMAXALTO;    
        
        //se crea la lista de fantasmas
        lklFantasmas = new LinkedList();
        
        // genero un numero azar de 8 a 10
        iAzar = (int) (Math.random() * 3) + 8;
       
        // genero cada fantasma y lo añado a la lista
        for (int iI = 0; iI < iAzar; iI ++) {
            // se crea el objeto para fantasma
            Base basFantasma = new Base(iPosX,iPosY, WIDTH / iMAXANCHO,
                      HEIGHT / iMAXALTO,
                      Toolkit.getDefaultToolkit().getImage(urlImagenFantasma));
            basFantasma.setX(-basFantasma.getAncho() + 
                                -((int) (Math.random() * WIDTH)));   
            basFantasma.setY((int) (Math.random() * 
                                   (HEIGHT - basFantasma.getAlto() - 25)) + 25);
            lklFantasmas.add(basFantasma);
        }
	
        //Creo el sonido de colision entre juanito y Nena
        URL urlSonidoJuanito = this.getClass().getResource("monkey1.wav");
        adcSonidoJuanito = new SoundClip("monkey1.wav");
        
        //Creo el sonido de colision entre fantasma y Nena
        URL urlSonidoFantasma = this.getClass().getResource("monkey2.wav");
        adcSonidoFantasma = new SoundClip("monkey2.wav");
        
        /* se le añade la opcion al JFrame de ser escuchado por los eventos
           del teclado  */
        addKeyListener(this);
        
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }

     /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas > 0 && !bEscape) {
            //si la pausa no esta activada
            if (!bPausa) {
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
        }
    }
    
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza() {
        //Dependiendo de la iDireccion de Nena es hacia donde se mueve.
        switch (iDireccion) {
            case 1: { //se mueve hacia arriba
                basNena.setY(basNena.getY() - 3);
                break;    
            }
            case 2: { //se mueve hacia abajo
                basNena.setY(basNena.getY() + 3);
                break;    
            }
            case 3: { //se mueve hacia izquierda
                basNena.setX(basNena.getX() - 3);
                break;    
            }
            case 4: { //se mueve hacia derecha
                basNena.setX(basNena.getX() + 3);
                break;    	
            }
        }
        
        // ciclo para mover cada juanito de la lista
        for (Base basJuanito : lklJuanitos) {
            //velocidad definida y se acelera dependiendo de las vidas
            int iVelocidad = 6 - iVidas;
            basJuanito.setY(basJuanito.getY() + iVelocidad);
        }
        
        // ciclo para mover cada fantasma de la lista
        for (Base basFantasma : lklFantasmas) {
            // genero un numero azar de 3 a 5
            int iAzar = (int) (Math.random() * 3) + 3;
            basFantasma.setX(basFantasma.getX() + iAzar);
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision() {
        //Colision de Nena con el JFrame dependiendo a donde se mueve.
        //Nena colisiona con el lado izquierdo
        if (basNena.getX() < 0) {
            basNena.setX(0);    //Nena no sale
        }
        //Nena esta colisionando con el lado derecho
        if (basNena.getX() + basNena.getAncho() > WIDTH) {
            basNena.setX(WIDTH - basNena.getAncho()); /*Nena
                                                                no sale*/
        }
        //Nena esta colsionando con el lado superior
        if (basNena.getY() < 25) { 
            basNena.setY(25);     //Nena no sale
        }
        //Nena esta colisionando con el lado inferior
        if (basNena.getY() + basNena.getAlto() > HEIGHT) { 
            basNena.setY(HEIGHT - basNena.getAlto()); /*Nena
                                                               no sale*/
        }
        
        // ciclo para revisar colision de los juanitos
        for (Base basJuanito : lklJuanitos) {
            //checo la colision con Nena
            if (basNena.intersecta(basJuanito)) {
                //Reubicar a juanito
                basJuanito.setX((int) (Math.random() * 
                                    (WIDTH - basJuanito.getAncho())));   
                basJuanito.setY(-basJuanito.getAlto() + 
                                -((int) (Math.random() * HEIGHT)));  
                //se suma una colision al contador
                iContadorJuanitos++;
                //si ya colisionaron 5
                if (iContadorJuanitos >= 5) {
                    iVidas--;   //se resta una vida
                    iContadorJuanitos = 0;
                }
                adcSonidoJuanito.play(); //sonido al colisionar con Nena
            }
            
            //si colisiono con la parte de abajo del JFrame
            if ((basJuanito.getY() + basJuanito.getAlto()) > HEIGHT) {
                //Reubicar a juanito
                basJuanito.setX((int) (Math.random() * 
                                    (WIDTH - basJuanito.getAncho())));   
                basJuanito.setY(-basJuanito.getAlto() + 
                                -((int) (Math.random() * HEIGHT)));      
            }
        }
        
        // ciclo para revisar colision de los fantasmas
        for (Base basFantasma : lklFantasmas) {
            //checo la colision con Nena
            if (basNena.intersecta(basFantasma)) {
                //Reubicar al fantasma
                basFantasma.setX(-basFantasma.getAncho() + 
                                         -((int) (Math.random() * WIDTH))); 
                basFantasma.setY((int) (Math.random() * 
                                   (HEIGHT - basFantasma.getAlto() - 25)) + 25);     
                iPuntos++;   //Al colisionar se aumenta 1 punto
                adcSonidoFantasma.play(); //sonido al colisionar con Nena
            }
            
            //cehoco si colisiono con el lado derecho
            if ((basFantasma.getX() + basFantasma.getAncho()) > WIDTH) {
                //Reubicar al fantasma
                basFantasma.setX(-basFantasma.getAncho() + 
                                         -((int) (Math.random() * WIDTH))); 
                basFantasma.setY((int) (Math.random() * 
                                    (HEIGHT - basFantasma.getAlto())));     
            }
        }
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
            imaImagenApplet = createImage (this.getSize().width, 
                                this.getSize().height);
            graGraficaApplet = imaImagenApplet.getGraphics ();
        }
        
        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, WIDTH, HEIGHT, this);
        
        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);
        
        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint1
     * 
     * Metodo sobrescrito de la clase <code>JFrame</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint1 (Graphics graDibujo) {
        //Si todavia hay vidas en el juego o no ESCAPE
        if (iVidas > 0 && !bEscape) {
            // si la imagen ya se cargo
            if (basNena != null && lklJuanitos != null && lklFantasmas != null) {
                //Dibuja la imagen de principal en el JFrame
                basNena.paint(graDibujo, this);

                // pinto cada Juanito de la lista
                for (Base basJuanito : lklJuanitos) {
                    //Dibuja la imagen de juanito en el JFrame
                    basJuanito.paint(graDibujo, this);
                }

                // pinto cada fantasma de la lista
                for (Base basFantasma : lklFantasmas) {
                    //Dibuja la imagen de juanito en el JFrame
                    basFantasma.paint(graDibujo, this);
                }

                //Dibujar vidas restantes
                graDibujo.setFont(new Font("Arial", Font.BOLD, 20));
                graDibujo.setColor(Color.red);
                String sVidasDisplay = "Vidas: " + Integer.toString(iVidas);
                graDibujo.drawString(sVidasDisplay, 690, 50);

                //Dibujar puntos
                String sPuntosDisplay = "Puntos: " + Integer.toString(iPuntos);
                graDibujo.drawString(sPuntosDisplay, 690, 80);

            } // sino se ha cargado se dibuja un mensaje 
            else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
        }
        
        else {
            // Dibuja game over
            graDibujo.setColor(Color.black);
            graDibujo.fillRect(0, 0, WIDTH, HEIGHT);
            //Dibujar imagenen
            graDibujo.drawImage (imaImagenGameOver, 
                           ((WIDTH / 2) - imaImagenGameOver.getWidth(this) / 2), 
                           ((HEIGHT / 2) - imaImagenGameOver.getHeight(this) / 2) 
                           ,this);
            //dibujar puntos
            graDibujo.setColor(Color.white);
            String sPuntosDisplay = "Puntos: " + Integer.toString(iPuntos);
            graDibujo.drawString(sPuntosDisplay, ((WIDTH / 2) - 50), 
                                    ((HEIGHT / 2) + 100));
        }
        
    }

    /**
     * keyTyped
     * 
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una 
     * tecla que no es de accion.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al 
     * presionar.
     * 
     */
    public void keyTyped(KeyEvent keyEvent) {
        // no hay codigo pero se debe escribir el metodo
    }

    /**
     * keyPressed
     * 
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al dejar presionada
     * alguna tecla.
     * Se cambia la direccion al presionar una tecla
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al 
     * presionar.
     * 
     */
    public void keyPressed(KeyEvent keyEvent) {
        // si presiono la tecla W
        if(keyEvent.getKeyCode() == KeyEvent.VK_W) {
            iDireccion = 1;
        }
        // si presiono la tecla S
        else if(keyEvent.getKeyCode() == KeyEvent.VK_S) {  
            iDireccion = 2;
        }
        // si presiono la tecla A
        else if(keyEvent.getKeyCode() == KeyEvent.VK_A) {    
            iDireccion = 3;
        }
        // si presiono la tecla D
        else if(keyEvent.getKeyCode() == KeyEvent.VK_D) {    
            iDireccion = 4;
        }
    }

    /**
     * keyReleased
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al soltar.
     * 
     */
    public void keyReleased(KeyEvent keyEvent) {
        // si presiono la tecla W
        if(keyEvent.getKeyCode() == KeyEvent.VK_W) {
            iDireccion = 1;
        }
        // si presiono la tecla S
        else if(keyEvent.getKeyCode() == KeyEvent.VK_S) {  
            iDireccion = 2;
        }
        // si presiono la tecla A
        else if(keyEvent.getKeyCode() == KeyEvent.VK_A) {    
            iDireccion = 3;
        }
        // si presiono la tecla D
        else if(keyEvent.getKeyCode() == KeyEvent.VK_D) {    
            iDireccion = 4;
        }
        // si presiono la tecla P
        else if(keyEvent.getKeyCode() == KeyEvent.VK_P) {  
            if (!bPausa) {
                bPausa = true;
            }
            else {
                bPausa = false;
            }
        }
        // si presiono la tecla escape
        else if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {  
            bEscape = true;
        }
        // si presiono la tecla c
        else if(keyEvent.getKeyCode() == KeyEvent.VK_C) {  
            try{
                grabaArchivo();    //Graba el vector en el archivo.
            }catch(IOException e){
                System.out.println("Error en " + e.toString());
            }
        }
        // si presiono la tecla v
        else if(keyEvent.getKeyCode() == KeyEvent.VK_V) {  
            try{
                leeArchivo();    //Graba el vector en el archivo.
            }catch(IOException e){
                System.out.println("Error en " + e.toString());
            }
        }
    }
    
    /**
    * Metodo que lee a informacion de un archivo y lo agrega a un vector.
    *
    * @throws IOException
    */
    public void leeArchivo() throws IOException{
    	BufferedReader fileIn;
    	try{
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	} catch (FileNotFoundException e){
    		File puntos = new File(nombreArchivo);
    		PrintWriter fileOut = new PrintWriter(puntos);
    		fileOut.println("100,demo");
    		fileOut.close();
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	}
        //leer archivo
        //vidas
    	String dato = fileIn.readLine();
        iVidas = (Integer.parseInt(dato));
        //puntos
        dato = fileIn.readLine();
        iPuntos = (Integer.parseInt(dato));
        
    	fileIn.close();
    }

    /**
     * Metodo que agrega la informacion del vector al archivo.
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException{
    	PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
        
        //grabar vidas y puntos
        fileOut.println(iVidas);
        fileOut.println(iPuntos);
        
        //grabar pos. de nena
        fileOut.println(basNena.getX());
        fileOut.println(basNena.getY());
        
        // ciclo para guardar cada pos. juanito de la lista
        for (Base basJuanito : lklJuanitos) {
           fileOut.println(basJuanito.getX());
           fileOut.println(basJuanito.getY());
        }
        
        // ciclo para guardar cada pos. fantasma de la lista
        for (Base basFantasma : lklFantasmas) {
            fileOut.println(basFantasma.getX());
            fileOut.println(basFantasma.getY());
        }
        
    	fileOut.close();	
    }
    
    /**
     * main
     * Metodo......
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExamenAppJFrame juego = new ExamenAppJFrame();
        juego.setSize(WIDTH, HEIGHT);
        juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juego.setVisible(true);
    }
}
