package comunicacion.servidor;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Clase que maneja la comunicación con un cliente específico
 * Se ejecuta en un hilo separado para cada cliente conectado
 * @author Systems S.A.
 */
public class ManejadorCliente implements Runnable {
    
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private String nombreUsuario;
    private String ip;
    private String horaConexion;
    private Servidor servidor;
    private boolean activo;
    
    /**
     * Constructor del manejador de cliente
     * @param socket Socket del cliente conectado
     * @param servidor Referencia al servidor principal
     */
    public ManejadorCliente(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
        this.activo = true;
        this.ip = socket.getInetAddress().getHostAddress(); // Obtener IP
        this.horaConexion = obtenerHora(); // Obtener hora de conexión
    }
    
    /**
     * Método principal que se ejecuta en el hilo
     * Maneja la comunicación con el cliente
     */
    @Override
    public void run() {
        try {
            // Inicializar streams de entrada/salida
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            // Leer la primera línea
            String primeraLinea = entrada.readLine();
            
            if (primeraLinea != null) {
                if (primeraLinea.equals("LIST")) {
                    // Manejar solicitud de lista de conexiones
                    List<String> usuarios = servidor.getConnectedUsers();
                    for (String user : usuarios) {
                        salida.println(user);
                    }
                    salida.println("END_LIST");
                    // Cerrar conexión inmediatamente
                    cerrar();
                } else {
                    // Es un cliente normal, primera línea es el nombre de usuario
                    nombreUsuario = primeraLinea;
                    
                    if (!nombreUsuario.trim().isEmpty()) {
                        servidor.clienteConectado(this); // Pasar this para info adicional
                        
                        // Notificar a todos que se conectó un nuevo usuario
                        servidor.broadcast("*** " + nombreUsuario + " se ha unido al chat ***", this);
                        
                        // Bucle principal para recibir mensajes del cliente
                        String mensaje;
                        while (activo && (mensaje = entrada.readLine()) != null) {
                            String horaActual = obtenerHora();
                            String mensajeCompleto = "[" + horaActual + "] " + nombreUsuario + ": " + mensaje;
                            
                            // Enviar mensaje a todos los clientes conectados
                            servidor.broadcast(mensajeCompleto, this);
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            if (activo) {
                System.err.println("Error en comunicación con " + nombreUsuario + ": " + e.getMessage());
            }
        } finally {
            // Cerrar conexión y limpiar recursos
            cerrar();
            if (nombreUsuario != null) {
                servidor.eliminarCliente(this);
                servidor.broadcast("*** " + nombreUsuario + " ha salido del chat ***", this);
            }
        }
    }
    
    /**
     * Envía un mensaje a este cliente específico
     * @param mensaje Mensaje a enviar
     */
    public void enviarMensaje(String mensaje) {
        if (salida != null && activo) {
            try {
                salida.println(mensaje);
            } catch (Exception e) {
                System.err.println("Error al enviar mensaje a " + nombreUsuario);
            }
        }
    }
    
    /**
     * Cierra la conexión con el cliente
     */
    public void cerrar() {
        try {
            activo = false;
            
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión con " + nombreUsuario + ": " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la hora actual en formato HH:mm:ss
     * @return String con la hora formateada
     */
    private String obtenerHora() {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        return formato.format(new Date());
    }
    
    /**
     * Obtiene el nombre de usuario de este cliente
     * @return Nombre de usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public String getIp() {
        return ip;
    }
    
    public String getHoraConexion() {
        return horaConexion;
    }
    
    /**
     * Obtiene info detallada del usuario
     * @return String con detalles (nombre, IP, hora)
     */
    public String getUsuarioInfo() {
        return nombreUsuario + " (IP: " + ip + ", Conectado a las: " + horaConexion + ")";
    }
    
    /**
     * Verifica si el cliente está activo
     * @return true si está activo, false en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }
}