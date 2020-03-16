package chapter2.item8;

import java.lang.ref.Cleaner;
//An autocloseable class using a cleaner as a safety net (page 32)
//使用清洁剂作为安全网的自动关闭类（页32）
public class Room implements AutoCloseable{
    private static final Cleaner cleaner = Cleaner.create();
    //Resource that requires cleaning. Must not refer to Room!

    /**需要清洁的资源，关键是State实例没有引用它的ROOM实例。
     * 如果它引用了，会造成循环，阻止ROOM实例被垃圾回收（以及防止被自动清除）
     * 因此State必须是一个静态的嵌套类，因为非静态的嵌套类包含了对其外围实例的引用
     */
    private static class State implements Runnable{
        int numJunkPiles; //Number of junk piles in this room
        State(int numJunkPiles){
            this.numJunkPiles =numJunkPiles;
        }
        //Invoked by close method or cleaner
        @Override public void run(){
            System.out.println("Cleaning room");
            numJunkPiles = 0;
        }

    }
    //The state of this room,shared with our cleanable
    //此房间的状态，与我们的可清洁房间共享
    private final State state;
    //Our cleanable. cleans the room when it's eligible for gc
   //我们可清洗。 可以打扫房间的时候打扫房间
    private final Cleaner.Cleanable cleanable;
    public Room(int numJunkPils){
        state = new State(numJunkPils);
        cleanable = cleaner.register(this,state);
    }
    @Override public void close(){
        cleanable.clean();
    }
}
