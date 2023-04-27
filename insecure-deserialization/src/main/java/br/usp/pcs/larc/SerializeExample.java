package br.usp.pcs.larc;

import java.io.*;

/**
 * This is an example of a payload.
 */
public class SerializeExample {

    public static void main(String[] args) {
        try {
            // Create a new instance of an object that implements the Serializable interface
            MyObject obj = new MyObject();

            // Create a ByteArrayOutputStream to write the serialized data to
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Create an ObjectOutputStream to write objects to the ByteArrayOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            // Write the object to the ObjectOutputStream
            oos.writeObject(obj);

            // Flush and close the ObjectOutputStream
            oos.flush();
            oos.close();

            // Get the serialized data as a byte array
            byte[] serializedData = baos.toByteArray();

            // Write the serialized data to a file named "serializedData.ser"
            FileOutputStream fos = new FileOutputStream("serializedData.ser");
            fos.write(serializedData);
            fos.close();

            System.out.println("Serialized data written to file successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class MyObject implements Serializable {
    public void run() {
        System.out.println("YOooo watch out! This is the InsecureDeserialization Backdoor");
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        // Override the default writeObject method to write only the class name
        oos.writeObject(getClass().getName());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        // Override the default readObject method to create a new instance of the class and run the 'run' method
        String className = (String) ois.readObject();
        try {
            Class<?> clazz = Class.forName(className);
            Object obj = clazz.newInstance();
            MyObject myObj = (MyObject) obj;
            myObj.run();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
