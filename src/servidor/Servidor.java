package comunicacion.servidor;

import gui.ServidorGUI;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del servidor
 * Gestiona las conexiones de los clientes
 * @author Systems S.A.
 */
public class Servidor {
    
    private static final int PUERTO = 5000;
    private ServerSocket serverSocket;
    private List<ManejadorCliente> clientes;
    private ServidorGUI gui;
    private boolean ejecutando;
    
    /**
     * Constructor del servidor
     * @param gui Referencia a la interfaz gráfica
     */
    public Servidor(ServidorGUI gui) {
        this.gui = gui;
        this.clientes = new ArrayList<>();
        this.ejecutando = false;
    }
    
    /**
     * Inicia el servidor y espera conexiones
     */
    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PUERTO);
            ejecutando = true;
            gui.agregarLog("Servidor iniciado en puerto " + PUERTO);
            
            // Bucle principal para aceptar clientes
            while (ejecutando) {
                try {
                    Socket clienteSocket = serverSocket.accept();
                    
                    // Crear manejador para el cliente
                    ManejadorCliente manejador = new ManejadorCliente(clienteSocket, this);
                    clientes.add(manejador);
                    
                    // Iniciar hilo para el cliente
                    Thread hiloCliente = new Thread(manejador);
                    hiloCliente.setDaemon(true);
                    hiloCliente.start();
                    
                } catch (IOException e) {
                    if (ejecutando) {
                        gui.agregarLog("Error al aceptar cliente: " + e.getMessage());
                    }
                }
            }
            
        } catch (IOException e) {
            gui.agregarLog("Error al iniciar servidor: " + e.getMessage());
        }
    }
    
    /**
     * Detiene el servidor
     */
    public void detener() {
        try {
            ejecutando = false;
            
            // Cerrar todas las conexiones de clientes
            for (ManejadorCliente cliente : clientes) {
                cliente.cerrar();
            }
            clientes.clear();
            
            // Cerrar el socket del servidor
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            
            gui.agregarLog("Servidor detenido");
            
        } catch (IOException e) {
            gui.agregarLog("Error al detener servidor: " + e.getMessage());
        }
    }
    
    /**
     * Envía un mensaje a todos los clientes conectados
     * @param mensaje Mensaje a enviar
     * @param remitente Cliente que envió el mensaje (para no reenviarle)
     */
    public synchronized void broadcast(String mensaje, ManejadorCliente remitente) {
        gui.agregarLog("Broadcast: " + mensaje);
        
        for (ManejadorCliente cliente : clientes) {
            if (cliente != remitente) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }
    
    /**
     * Elimina un cliente de la lista
     * @param cliente Cliente a eliminar
     */
    public synchronized void eliminarCliente(ManejadorCliente cliente) {
        clientes.remove(cliente);
        gui.eliminarUsuario(cliente.getUsuarioInfo());
        gui.agregarLog(cliente.getNombreUsuario() + " se ha desconectado");
    }
    
    /**
     * Agrega información de un nuevo cliente
     * @param manejador Manejador del cliente conectado
     */
    public void clienteConectado(ManejadorCliente manejador) {
        gui.agregarUsuario(manejador.getUsuarioInfo());
        gui.agregarLog(manejador.getNombreUsuario() + " se ha conectado desde " + manejador.getIp() + " a las " + manejador.getHoraConexion());
    }
    
    /**
     * Obtiene la lista de usuarios conectados con info adicional
     * @return Lista de strings con detalles de usuarios conectados
     */
    public synchronized List<String> getConnectedUsers() {
        List<String> users = new ArrayList<>();
        for (ManejadorCliente cliente : clientes) {
            if (cliente.getNombreUsuario() != null) {
                users.add(cliente.getUsuarioInfo());
            }
        }
        return users;
    }
}