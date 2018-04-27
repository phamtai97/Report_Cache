> Tên: Phạm Tĩnh Hồng Tấn Tài
# 1. Cache

----
## Cache là gì?
>Là vùng nhớ lưu trữ tạm thời các file dữ liệu nhất định của thiết bị.
Nội dung lưu có thể là: logo, banner, hình ảnh tĩnh, các file định dạng css, javascript, tập tin có thể tải về, tập tin media,...



----
## Sử dụng cache ở đâu?
1. Có thể được được ở phía server, client.
   ![](https://i.imgur.com/oXwZWSX.jpg)
   [Response cache] (https://www.embedthis.com/appweb/doc/users/caching.html)

2. CDN cũng có bộ nhớ cache.
  ![](https://i.imgur.com/fgrAM4W.png)
 * Link tham khảo: 

  [CDN là gì và khi nào dùng CDN cho website] (https://thachpham.com/hosting-domain/cdn-la-gi-va-khi-nao-nen-dung-cdn-cho-website.html)
  
  [CDN Caching](https://www.incapsula.com/cdn-guide/cdn-caching.html)
3. Server web: máy chủ web cũng có thể dùng cache, lúc đó không cần gửi yêu cầu tới các máy chủ con. Reveser proxy và Forward proxy cũng sử dụng cache.
   * Link tham khảo:

 [Forward proxy and Reveser proxy](http://www.jscape.com/blog/bid/87783/Forward-Proxy-vs-Reverse-Proxy)

 [ Web Caching Basics: Terminology, HTTP Headers, and Caching Strategies](https://www.digitalocean.com/community/tutorials/web-caching-basics-terminology-http-headers-and-caching-strategies)

4. Database caching: bộ nhớ cache sẽ lưu lại kết quả của những câu query thường xuyên sử dụng trên database -> những lần truy vấn sau chỉ cần lấy kết quả từ cache.
![](https://i.imgur.com/jrBlVNa.png)

5. Application cache:
        
 * **Memcached**: Hệ thống lưu trữ bộ nhớ lưu lại bản sao đối tượng và dữ liệu được truy cập nhiều lần để tăng tốc độ truy xuất. Dữ liệu lưu trữ dưới dạng key-value. Mục đích chính của nó là để tăng tốc độ của các ứng dụng web bằng cách truy vấn cơ sở dữ liệu từ bộ nhớ đệm, giúp giảm số lần đọc vào bộ nhớ chính.
    * Cách hoạt động:
        * Nhận request từ phía client -> kiểm tra trong memcached:
            * Nếu có trong memcached thì trả ngay kết quả về cho người dùng.
            * Nếu không có trong memcached thì truy vấn vào database -> lấy kết quả trả về cho người dùng, đồng thời lưu kết quả vào trong memcached.
   
    * Ưu điểm:
        * Ở mức nhỏ, người ta thường dùng memcached để làm nơi lưu trữ dữ liệu chia sẻ, thường là lưu session.
        * Ở mức lớn hơn, người ta dùng memcached để giảm số lần đọc từ database.
    
    * Nhược điểm:
        * Memcached không có cơ chế thẩm định tính chính xác của dữ liệu lưu trong nó.
        * Chưa đồng bộ tự động với dữ liệu database khi dữ liệu thay đổi.
  * **Redis**: Là 1 hệ thống lưu trữ key-value.
Redis có hỗ trợ nhiều cấu trúc dữ liệu cơ bản(hash, list, set, sorted set, string), giúp việc thao tác với dữ liệu tốt hơn các hệ thống cũ như memcached rất nhiều. Bên cạnh lưu trữ key-value trên RAM giúp tối ưu performance, redis còn có cơ chế sao lưu dữ liệu trên đĩa cứng cho phép phục hồi dữ liệu khi gặp sự cố.
     * Có hai loại file ghi xuống đĩa cứng: 

        * **RDB (Redis DataBase file)**: RDB thực hiện tạo và sao lưu snapshot của database vào ổ cứng sau mỗi khoảng thời gian nhất định. 
            * Ưu điểm: 
                * RDB cho phép lưu các version khác nhau của database -> thuận tiện khi có sự cố xảy ra. 
                
                * Bằng việc lưu trữ data vào 1 file cố định -> dễ dàng chuyển data đến các data centers, máy chủ khác nhau. 
                * RDB giúp tối ưu hóa hiệu năng của Redis. Tiến trình Redis chính sẽ chỉ làm các công việc trên RAM, bao gồm các thao tác cơ bản được yêu cầu từ phía client như thêm/đọc/xóa, trong khi đó 1 tiến trình con sẽ đảm nhiệm các thao tác disk I/O.
            * Nhược điểm: 
                * Trong trường hợp có sự cố, Redis không thể hoạt động, dữ liệu đang tạo snapshot để lưu xuống database sẽ bị mất.
                * RDB cần dùng fork() để tạo tiến trình con phục vụ cho thao tác disk I/O. Trong trường hợp dữ liệu quá lớn, quá trình fork() có thể tốn thời gian và server sẽ không thể đáp ứng được request từ client.  
       
        * **AOF (Append Only File)**: AOF lưu lại tất cả các thao tác viết mà server nhận được, các thao tác này sẽ được chạy lại khi restart server hoặc tái thiết lập dataset(một đối tượng chứa nhiều datatable được lưu trong bộ nhớ) ban đầu.
             * Ưu điểm:
                * Người dùng có thể config để Redis ghi log theo từng câu query hoặc mỗi giây 1 lần. Redis ghi log AOF theo kiểu thêm vào cuối file sẵn có.Ngoài ra, kể cả khi chỉ 1 nửa câu lệnh được ghi trong file log (có thể do ổ đĩa bị full), Redis vẫn có cơ chế quản lý và sửa chữa lỗi đó (redis-check-aof). 
                * Redis cung cấp tiến trình chạy nền, cho phép ghi lại file AOF khi dung lượng file quá lớn. Trong khi server vẫn thực hiện thao tác trên file cũ, Và 1 khi file mới được ghi xong, Redis sẽ chuyển sang thực hiện thao tác ghi log trên file mới.
           
            * Nhược điểm:
                * File AOF thường lớn hơn file RDB với cùng 1 dataset.
   
                * AOF có thể chậm hơn RDB tùy theo cách thức thiết lập khoảng thời gian cho việc sao lưu vào ổ cứng. Tuy nhiên, nếu thiết lập log 1 giây 1 lần có thể đạt hiệu năng tương đương với RDB.

 * Có hai nhiều cấp độ khi lưu vào cache: 
     * **Caching at the database query level**: Khi truy vấn vào database, băm câu truy vấn thành khóa và lưu result vào cache. Nhược điểm là khó xóa result cho những câu truy vấn phức tạp, nếu một dữ liệu thay đổi phải xóa hết những kết quả của những câu truy vấn đến dữ liệu đó.

     * **Caching at the object level**: xem dữ liệu như một đối tượng, tập hợp các dữ liệu thành một lớp hoặc đối tượng. Xóa đối tượng ra khỏi cache khi mà dữ liệu cơ bản bị thay đổi. Cho phép dùng bất đồng bộ (sử dụng các đối tượng mới nhất). 

----
## Khi nào cần cập nhật bộ nhớ cache?

>Bộ nhớ cache có giới hạn, phải có phương án cập nhật dữ liệu cho cache một cách hợp lí để tối ưu được bộ nhớ cache.

### 1. Cache-aside:

![](https://i.imgur.com/x17OdLz.png)

  * Cache và database độc lập với nhau.

  * Cách hoạt động: 
     * Đọc: Tìm dữ liệu trong cache -> nếu có trả về cho người dùng. Nếu không có -> tìm trong database -> thêm dữ liệu tìm được trong database vào cache -> trả kết quả về người dùng.
     * Ghi và cập nhật trực tiếp vào database.

  * Ưu điểm: Tránh lưu những dữ liệu không cần thiết.

  * Nhược điểm: 

     * Dữ liệu trong cache không chính xác khi database được cập nhập (theo quy trình trên cache không bị cập nhật) -> cách giải quyết: sử dụng TTL (Time to live) cho dữ liệu được luư trong cache hoặc dụng cách Write-Through (nói ở phía dưới).

     * Gây sự chậm trễ vì qua 3 bước.


`Memcached thường sử dụng cách này để lưu dữ liệu trong cache.`

### 2.Write-through: 

![](https://i.imgur.com/vnUaj8n.png)

 * Ứng dụng người dùng sử dụng cache là nơi lưu trữ dữ liệu chính để đọc và ghi dữ liệu, trong khi đó cache sẽ đồng bộ đọc và ghi dữ liệu vào database(mỗi lân cập nhật, dữ liệu đều được cập nhật trong cache sau đó cập nhật trong database). Người dùng không đọc hay ghi trực tiếp vào database.

 * Cách hoạt động: Người dùng đọc, ghi, cập nhật dữ liệu vào cache -> cache đồng bộ đọc, ghi, cập nhật vào database -> trả kết quả.

 * Ưu điểm: Dữ liệu trong cache không bị cũ so với database.

 * Nhước điểm: Hầu hết các dữ liệu ghi trong cache đêu không được đọc ra -> không cần thiết lưu lại những dữ liệu này trong cache -> sử dụng TTL.

### 3.Write-behind (write-back)

 ![](https://i.imgur.com/AeYVpYm.png)

 * Cache có sử dụng `Queue` để lưu lại các thao tác thêm, cập nhật dữ liệu.

 * Cách hoạt động: Người dùng thêm/cập nhật dữ liệu vào cache, cache sẽ đưa sự kiện thêm,cập nhật vào queue, và đồng thời ghi không đồng bộ dữ liệu từ queue vào database.

 * Ưu điểm: Việc sử dụng `Queue` giúp cho việc ghi nhanh hơn vì ghi đồng thời cả vào cache và database (bất đồng bộ).
 * Nhược điểm: 	Dữ liệu có thể bị mất khi mà cache bị hỏng -> `Queue` hỏng -> dữ liệu chưa được ghi xuống database.

### 4.Refresh-ahead

![](https://i.imgur.com/jFQt2tH.png)

 * Trước khi hết hạn của dữ liệu trong cache (hết thời gian TTL) thì cache sẽ cho làm mới tất cả dữ liệu trong cache.
 
 * Ưu điểm: dữ liệu trong cache luôn được cập nhật mới nhất.

 * Nhược điểm: Không biết dữ liệu nào sẽ cần cho sau này -> việc cập nhật sẽ những dữ liệu này gây ra dư thừa, tốn bộ nhớ lưu trữ.

## Cache sử dụng thuật toán gì?

> Thuật toán cache khá đơn giản, chỉ cần một hash table để lưu giữ giá trị của từng key. Khi đó, việc set cache, và get cache sẽ có độ phức tạp trung bình là O(1).
Tuy nhiên đời không như mơ, do memory là giới hạn, nên khi cache đầy, ta cần phải loại bỏ một số phần tử ra khỏi cache. Có hai thuật toán hay dùng đó là **`LRU`** và **`LFU`**.

### 1. Least Recently Used (LRU).

![](https://i.imgur.com/Z5jSoNN.png)

* Thuật toán LRU lựa chọn những phần tử nào mà **lâu chưa được dùng tới** nhất.

* Một key được định nghĩa là “dùng”, khi có một thao tác get hoặc set vào key đó.

* Để có thể cài đặt LRU cache, sẽ cần phải có 2 cấu trúc dữ liệu: một cấu trúc để lưu cặp key-value, cấu trúc này dễ thấy sẽ là **HashMap**, và một cấu trúc để lưu và thay đổi vị trị các key theo chiều tăng dần theo thời gian. Thuật toán của chúng ta sẽ phụ thuộc nhiều vào cấu trúc để lưu các key.

* Có mấy quan sát cho chúng ta khi xem xét cấu trúc dữ liệu lưu các key đó là:

  * Cấu trúc này cần có thao tác remove phần tử đầu tiên (phẩn tử có thời gian truy cập nhỏ nhất) (1).

  * Cấu trúc này cần có thao tác chuyển vị trị 1 phẩn tử bất kỳ về cuối (hay là set timestamp lên (2) timestamp hiện tại).

  * Cấu trúc này cần có thao tác để search 1 phần tử theo key.

> Để search thì tốc độ tốt nhất là O(logN), nên nếu muốn xây dựng thuật toán tốt hơn O(logN), chúng ta cần loại bọ thao tác cuối. Chúng ta sẽ dùng HashMap, HashMap có thể lưu trữ luôn vị trí của key.

 * Giờ chỉ cần thao tác (1) và (2). Do đó, thay vì cần sử dụng PriorityQueue, ta sẽ dùng **Double Linked List**, và lưu thêm head, tail của List này là có thể thực hiện 2 thao tác kia bằng O(1).

 * Link tham khảo:
 [Thuật toán LRU cache](https://techtalk.vn/thuat-toan-lru-cache.html)

### 2. Thuật toán Least Frequently Used (LFU)

![](https://i.imgur.com/bjcTEnr.jpg)

* Thuật toán LFU lựa chọn những phần tử nào mà **ít được sử dụng thường xuyên** nhất.

* Tương tự như thuật toán **`LRU`**, **`LFU`** cũng dùng cấu trúc dữ liệu là **HashMap** để lưu các cặp key-value -> độ phức tạp truy xuất phần từ O(1) , và một **Double Linked list** để lưu tần suất truy cập.

* Tần suất tối đa được giới hạn bởi bộ cache để tránh tạo ra nhiều danh sách tần số. Ví dụ: ta có bộ nhớ cache max là 4 thì chúng ta sẽ có 4 tần số khác nhau (0->4). Mỗi tần số sẽ có một danh sách liên kết kép để theo dõi các mục đươc truy cập trong bộ nhớ cache có tần số tương ứng. Vấn đề đưa ra cần có một cấu trúc dữ liệu liên kết các danh sách tần suất này lại??? -> Ta có thể dùng mảng hoặc danh sách liên kết đôi.

* Link tham khảo: 
   
   [Java Articles LFU Cache](http://www.javarticles.com/2012/06/lfu-cache.html)

   [Least frequently used cache eviction scheme with complexity O(1) in Python](https://www.laurentluce.com/posts/least-frequently-used-cache-eviction-scheme-with-complexity-o1-in-python/)

   [An O(1) algorithm for implementing the LFU
cache eviction scheme](http://dhruvbird.com/lfu.pdf)

# 2. Caffeine cache

> **Caffeine** là thư viện cache hiệu năng cao cho Java 8. Caffeine tương tự như Map, sự khác biệt cơ bản nhất là Map thì nó giữ toàn bộ các key-value cho đến khi được xóa một cách chủ động. Trong khi đó cache của Caffeine thì có cơ chế để tự động "trục xuất" các key-value một cách tự động.

* Caffeine cung cấp cấu trúc linh hoạt để tạo nên bộ cache với sự kết hợp của các tính năng sau:

  * Tự động nạp các mục vào cache, có tùy chọn không đồng bộ.

  * Thu hồi kích thước size-based khi vượt quá giá trị max dựa trên tần suất và số lần truy cập gần đây.

  * Thời gian hết hạn của các mục, được tính từ thời điểm ghi/đọc gần nhất.

  * Làm mới (refress) bất đồng bộ khi xuất hiện yêu cầu ban đầu cho một entry.

  * Thống kê truy cập.

  * Link tham khảo: 
    
    [Caffeine cache trong java](https://viblo.asia/p/caffeine-cache-trong-java-WAyK8k7W5xX)

    [Introduction to Caffeine](http://www.baeldung.com/java-caching-caffeine)

    [Caffeine cache](https://github.com/ben-manes/caffeine)

    [Install maven](https://www.mkyong.com/maven/how-to-install-maven-in-ubuntu/) 

# 3. Vai trò Cache

* Tăng tốc độ phản hồi dữ liệu -> tăng tính trải nghiệm cho người dùng cảm thấy hệ thống nhanh hiệu quả.

* Cân bằng tải -> giảm số lần truy cập vào server -> Bảo vệ server.

* Giảm băng thông. 

* Giảm chi phí mạng: những nội dung dữ liệu mà người dùng truy cập nằm ở bộ nhớ cache của máy chủ con gần người dùng -> chỉ cần truy cập vào máy chủ con, không cần tốn chi phí truy cập tới máy chủ chính.

* Khi mạng gián đoạn vẫn có thể load được những dữ liệu tĩnh được lưu trên cache.

