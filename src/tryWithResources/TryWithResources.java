package tryWithResources;

public class TryWithResources {

    public static void main(String[] args) {
        try (DemoOne demoOne = new DemoOne(); DemoTwo demoTwo = new DemoTwo()) {
            /* 10 / 0 bize exception firlatacagi icin demoOne.show() ve demoTwo.show() methodlari execute
            * edilmeyecek fakat class'lar AutoCloseable interface'ini implement ettikleri icin ilk once DemoTwo
            * ardindan DemoOne close methodlari calistirilacak
            * */
            int x = 10 / 0;
            demoOne.show();
            demoTwo.show();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

// Custom resource 1
class DemoOne implements AutoCloseable {

    void show() {
        System.out.println("DemoOne class");
    }

    @Override
    public void close() throws Exception {
        System.out.println("close from DemoOne");
    }
}

// Custom resource 2
class DemoTwo implements AutoCloseable {

    void show() {
        System.out.println("DemoTwo class");
    }

    @Override
    public void close() throws Exception {
        System.out.println("close from DemoTwo");
    }
}