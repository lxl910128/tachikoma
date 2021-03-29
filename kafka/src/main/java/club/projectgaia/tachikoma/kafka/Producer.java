package club.projectgaia.tachikoma.kafka;


public interface Producer {
    
    void send(String key,String msg);
    
    void close();
    
    // add your interface method here
    
    // and then implement it
    
}
