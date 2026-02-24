package evgeniipetrov;

import com.digitalpetri.modbus.exceptions.ModbusExecutionException;
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport;
import com.digitalpetri.modbus.client.ModbusTcpClient;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        System.out.println("Hello World!");

        var transport = NettyTcpClientTransport.create(
                cfg->{
                    cfg.setHostname("10.0.6.10");
                    cfg.setPort(506);
                }
        );
        var client = ModbusTcpClient.create(transport);

        try {
            client.connect();
        }
        catch (ModbusExecutionException err){
            System.err.println("ModbusTcpClientTransport failed to connect: " + err.getMessage());
        }
        finally {
            try{
            client.disconnect();
        }

            catch (ModbusExecutionException err){
                System.err.println("ModbusTcpClientTransport failed to disconnect: " + err.getMessage());}
        }



    }
}
