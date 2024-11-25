package com.notificationprocessor.notificationprocessor.crossCutting.Messages;

public class UtilMessagesServices {
    public static final class BuzonNotificacionService{
        public static final String BUZON_GUARDADO = "¡Buzon guardado con exito!";

        public static final String NOTIFICACION_ELIMINADA = "¡Notificacion eliminada con exito!";
        public static final String NOTIFICACION_NO_ELIMINADA =  "Error al eliminar notificación: {}";

        public static final String BUZON_NOTIFICACION_ELIMINADO = "¡Buzon y propietario eliminado con exito!";

        public static final String DESTINATARIO_NO_ELIMINADO =  "Error al eliminar DESTINATARIO: {}";

    }

    public static final class ReciverMessageNotificacion{
        public static final String NOTIFICACION_GUARDADA = "Notificación guardada con éxito.";
        public static final String NOTIFICACION_NO_GUARDADA = "Error al guardar la notificación: {}";

        public static final String MENSAJE_NO_MAPEADO = "Mensaje recibido no pudo ser mapeado: {}";

    }
}
