package evgeniipetrov;

import com.digitalpetri.modbus.Modbus;
import com.digitalpetri.modbus.tcp.Netty;
import com.digitalpetri.modbus.exceptions.ModbusExecutionException;
import com.digitalpetri.modbus.exceptions.ModbusResponseException;
import com.digitalpetri.modbus.exceptions.ModbusTimeoutException;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersResponse;
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
            while(true) {
                try {
                    ReadHoldingRegistersResponse response = client.readHoldingRegisters(
                            1,
                            new ReadHoldingRegistersRequest(0, 12)
                    );
                    System.out.println("Response: " + response);
                    Thread.sleep(500);
                }
                catch (ModbusExecutionException err) {
                    System.err.println("ModbusTcpClientTransport (execution) failed to read holding registers: " + err.getMessage());
                }
                catch (ModbusTimeoutException err) {
                    System.out.println("ModbusTcpClientTransport failed to read holding registers: " + err.getMessage());
                }
                catch (ModbusResponseException err) {
                    System.err.println("ModbusTcpClientTransport (response) failed to read holding registers: " + err.getMessage());
                }
                catch (InterruptedException err){
                    Thread.currentThread().interrupt();
                    break;
                }
            }

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
            Modbus.releaseSharedResources();
            Netty.releaseSharedResources();
        }

    }
}
