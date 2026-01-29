package comunicacion.cliente;

import gui.ClienteGUI;
import java.io.*;
import java.net.Socket;

public class Cliente {
    
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private String usuario;
    private ClienteGUI gui;
    private boolean conectado;
    

    public Cliente(String host, int puerto, String usuario) throws IOException {
        this.usuario = usuario;
        socket = new Socket(host, puerto);
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        conectado = true;

        salida.println(usuario);

        Thread hiloReceptor = new Thread(() -> {
            try {
                String mensaje;
                while (conectado && (mensaje = entrada.readLine()) != null) {
                    if (gui != null) {
                        gui.agregarMensaje(mensaje);
                    }
                }
            } catch (IOException e) {
                if (conectado) {
                    System.err.println("Error en recepción: " + e.getMessage());
                }
            } finally {
                desconectar();
            }
        });
        hiloReceptor.setDaemon(true); 
        hiloReceptor.start();
    }

    public void enviarMensaje(String mensaje) {
        if (conectado && salida != null) {
            salida.println(mensaje);
        }
    }

    public void desconectar() {
        if (!conectado) return;
        
        conectado = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.shutdownInput(); 
                socket.close();
            }
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }

    public void setGui(ClienteGUI gui) {
        this.gui = gui;
    }
}