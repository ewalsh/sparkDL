package ai.economicdatasciences.dataServer

import java.io.DataOutputStream
import java.net.{ServerSocket, Socket}
import java.util.Scanner

object SocketWriter {
    def main(args: Array[String]): Unit = {
        val listener = new ServerSocket(9999)
        val socket = listener.accept()
        val outputStream = new DataOutputStream(socket.getOutputStream())
        System.out.println("Start writing data. Enter close when finished")
        val scan = new Scanner(System.in)
        var str = ""
        while(!(str = scan.nextLine()).equals("close")){
            outputStream.writeUTF(str)
        }
        outputStream.close()
        listener.close()
    }
}
