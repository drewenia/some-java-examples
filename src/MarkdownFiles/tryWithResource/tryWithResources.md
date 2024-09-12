Java'da Try-with-resources deyimi, içinde bir veya daha fazla resource bildiren bir try deyimidir. Resource, programınız
onu kullanmayı bitirdiğinde kapatılması gereken bir nesnedir. Örneğin, bir File kaynağı veya bir Socket bağlantısı
kaynağı. try-with-resources deyimi, her bir resource'un state yürütmesinin sonunda kapatılmasını sağlar. Resource'ları
kapatmazsak, bu bir resource sızıntısı oluşturabilir ve ayrıca program mevcut kaynakları tüketebilir.

java.lang.AutoCloseable'ı implement eden herhangi bir nesneyi resource olarak aktarabilirsiniz; bu, java.io.Closeable'ı
implement eden tüm nesneleri içerir.

Bu sayede, artık sadece resource'ların kapanış statement'larını iletmek için fazladan bir finally bloğu eklememize gerek
kalmadı. try-catch bloğu çalıştırılır çalıştırılmaz resource'lar kapatılacaktır.

```
try(declare resources here) {
    // use resources
}
catch(FileNotFoundException e) {
    // exception handling
}
```

### Exceptions

Exception söz konusu olduğunda, try-catch-finally bloğu ile try-with-resources bloğu arasında bir fark vardır. Hem try
bloğunda hem de finally bloğunda bir exception throw edilirse, method finally bloğunda atılan exception'ı döndürür.

try-with-resources için, bir try bloğunda ve try-with-resources statement'ında bir exception throw edilirse, method try
bloğunda atılan exception'ı döndürür. try-with-resources tarafından throw edilen exception'lar bastırılır, yani
try-with-resources bloğunun bastırılmış exception'lar throw ettiğini söyleyebiliriz.

### Case 1 - Single Resources

```
public static void main(String[] args) {

    /* Exception'ları kontrol etmek icin try blogu
     * Stream veya raw data yazmak için bir FileOutputStream nesnesi oluşturma
     *  */
    try (FileOutputStream fos = new FileOutputStream("gfgTextFile.txt")) {
        String text = "Hello World. This is my java program";
        byte[] arr = text.getBytes();
        fos.write(arr);
    } catch (Exception ex) {
        System.out.println(ex);
    }

    System.out.println("Resource are closed and message has been written into txt file");
}
```

![img.png](img.png)

### Case 2 - Multiple Resources

```
public static void main(String[] args) {
    try (FileOutputStream fos = new FileOutputStream("outputFile.txt");
         BufferedReader br = new BufferedReader(new FileReader("gfgTextFile.txt"))) {

        String text;

        while((text = br.readLine()) != null){
            byte[] arr = text.getBytes();
            fos.write(arr);
        }

        System.out.println("File content copied to another one");

    } catch (Exception ex) {
        System.out.println(ex);
    }

    System.out.println("Resource are closed and message has been written into the gfgTextFile.txt");
}
```

Case 2'de multiple resources tanimladik. gfgTextFile.txt icerigini outputFile.txt icerisine kopyaladik

### JAVA SE 9 : Gelistirilmis try-with-resources statement'lari

java 7 veya 8'de bir resource try-with-resources statement'inin dışında zaten declare edildiyse, onu local variable ile
yeniden göndermeliyiz. Bu da try bloğu içinde yeni bir variable bildirmemiz gerektiği anlamına gelir. Yukarıdaki
argümanı açıklayan koda bakalım:

```
public static void main(String[] args) throws IOException {
    File file = new File("hello.txt");

    BufferedReader br = new BufferedReader(new FileReader(file));

    try(BufferedReader reader = br){
        String text = reader.readLine();
        System.out.println(text);
    }
}
```

Java 9'da bu local variable'i oluşturmamız gerekmez. Bunun anlamı, eğer bir try-with-resources statement'i dışında
final veya effective final olarak bildirilmiş bir resource'umuz varsa, bildirilen resource'a atıfta bulunan bir
local variable oluşturmamız gerekmez, bildirilen resource herhangi bir sorun olmadan kullanılabilir. Yukarıdaki
argümanı açıklayan java koduna bakalım.

```
public static void main(String[] args) throws IOException {
    File file = new File("hello.txt");

    BufferedReader br = new BufferedReader(new FileReader(file));

    try (br) {
        String text = br.readLine();
        System.out.println(text);
    }
}
```

### Automatic Resource Management in multiple resources

Bir try-with-resources bloğu içinde birden fazla resource kullanılabilir ve hepsinin otomatik olarak kapatılması
sağlanabilir. Bu durumda, resource'lar parantez içinde oluşturuldukları sıranın tersine göre kapatılacaktır.

```
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
```

