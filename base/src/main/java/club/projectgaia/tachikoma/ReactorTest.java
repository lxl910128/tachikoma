package club.projectgaia.tachikoma;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Phoenix Luo
 * @version 2021/1/1
 **/
public class ReactorTest {
    public static void main(String[] args) {
        //createFlux();
        //createMono();
        // operationBuff();
        // operationFilter();
        // operationWindow();
        // operationZip();
        // operationTake();
        // operationReduce();
        // operationMerge();
        // operationMap();
        // combineLatest();
        // subscribe();
        //scheduler();
        //log();
        // hotAndCloud();
        then();
    }
    
    public static void then() {
        //无从接受上一步的执行结果
        Flux.range(1, 100).then().subscribe(System.out::println);
    }
    
    //冷序列的含义是不论订阅者在何时订阅该序列，总是能收到序列中产生的全部消息。
    //而与之对应的热序列，则是在持续不断地产生消息，订阅者只能获取到在其订阅之后产生的消息。
    // 之前都是冷的
    public static void hotAndCloud() {
        final Flux<Long> source = Flux.interval(Duration.ofMillis(1000))
                .take(10)
                //把一个 Flux 对象转换成 ConnectableFlux 对象
                .publish()
                // 当 ConnectableFlux 对象有一个订阅者时就开始产生消息。
                .autoConnect();
        // 设置订阅者，开始消费消息
        source.subscribe();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        
        }
        // 打印不全
        source
                .toStream()
                .forEach(System.out::println);
    }
    
    public static void log() {
        // 打日志
        Flux.range(1, 2).log().subscribe(System.out::println);
    }
    
    //通过调度器（Scheduler）可以指定上述操作执行的方式和所在的线程。
    // 比较迷没看懂有啥意义
    public static void scheduler() {
        Flux.interval(Duration.ofMillis(1000))
                //单一的可复用的线程
                //.publishOn(Schedulers.single())
                //.map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                //使用弹性的线程池
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                //使用对并行操作优化的线程池
                //.subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);
    }
    
    // 消费 消费时可以对出错数据专项处理
    public static void subscribe() {
        /*
        Flux.just(1, 2)
                // flux 拼接另一个flux
                .concatWith(Mono.error(new IllegalStateException()))
                // 消费时同时制定 正确的消费以及错误的消费方式
                .subscribe(System.out::println, System.err::println);
        
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                // 错误值 用 0 代替
                .onErrorReturn(0)
                .subscribe(System.out::println);
        
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                // 重试2次 ，本例中1，2会打印3次
                .retry(2)
                // error单独处理
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                })
                .subscribe(System.out::println);
        */
        Flux.range(0, 100).filter(x -> {
            System.out.println(x % 10 == 0);
            return x % 10 == 0;
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                // subscription 表示订阅关系
                System.out.println("onSubscribe," + subscription.getClass());
                // subscription 通过 request 来触发 onNext，无此 前面流程不执行
                subscription.request(10);
            }
            
            @Override
            public void onNext(Integer s) {
                // 调用 onNext 才会触发调用 filter
                System.out.println("currrent value is = " + s);
            }
            
            @Override
            public void onError(Throwable throwable) {
                System.out.println("it's error.");
            }
            
            @Override
            public void onComplete() {
                System.out.println("it's completed.");
            }
        });
        
    }
    
    public static void combineLatest() {
        //把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
        //只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。
        // 未产生新数据的流 取最后1个
        Flux.combineLatest(
                // 处理组合的方式
                (Function<Object[], Object>) objects -> String.join(";", Arrays.asList(objects).stream().map(Object::toString).collect(Collectors.toList())),
                Flux.interval(Duration.ofMillis(222)).take(5),
                Flux.interval(Duration.ofMillis(333)).take(5)
        ).toStream().forEach(System.out::println);
    }
    
    public static void operationMap() {
        //flatMap和flatMapSequential的订阅是同时进行的，而concatMap的是有先后顺序的
        //concatMap和flatMapSequential的值是跟源中值顺序相同，其中flatMapSequential是经过后排序，二者输出相同
        //flatMap中的值是交错的，根据事件触发
        
        // 类似spark 1个值出多值，多个多值的每个值组成1个flux
        Flux.just(5, 10)
                //入参单值 出参都是Flux
                // 2flux交织
                .flatMap(x -> Flux.interval(Duration.ofMillis(x * 100)).take(x))
                .toStream()
                .forEach(System.out::println);
        System.out.println("------------");
        // 同spark
        Flux.just(5, 10)
                //入参出参都是Flux
                .map(x -> x * 3)
                .toStream()
                .forEach(System.out::println);
        // 类似spark 1个值出多值，多个多值的每个值组成1个flux
        System.out.println("------------");
        Flux.just(5, 10)
                //入参单值 出参都是Flux
                // 2flux按顺序
                .flatMapSequential(x -> Flux.interval(Duration.ofMillis(x * 100)).take(x))
                .toStream()
                .forEach(System.out::println);
        System.out.println("------------");
        // 订阅者也是有先后顺序的，与flatMapSequential相比，都是第1flux输出完，第二个输出
        // 但是flatMapSequential第二个flux刚开始输出时明显是快的，应为第一个在处理时他也开始了
        // concatMap是正儿八经，第一个flux消费完，第二个消费
        Flux.just(5, 10)
                //入参单值 出参都是Flux
                // 2flux按顺序
                .concatMap(x -> Flux.interval(Duration.ofMillis(x * 100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }
    
    // 合并
    public static void operationMerge() {
        // 2个flux交至进行
        Flux.merge(Flux.interval(Duration.ofSeconds(1L)).take(5), Flux.interval(Duration.ofSeconds(1L)).take(5))
                .toStream()
                .forEach(System.out::println);
        // A接完接B
        Flux.mergeSequential(Flux.interval(Duration.ofSeconds(1L)).take(5), Flux.interval(Duration.ofSeconds(1L)).take(5))
                .toStream()
                .forEach(System.out::println);
    }
    
    // 同spark 不赘述
    public static void operationReduce() {
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        // 100是初试值，100+1+2+3....
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }
    
    // 取元素，按个数，暗时间，从头，条件
    public static void operationTake() {
        // 1-1000 取10个
        Flux.range(1, 1000).take(10).subscribe(System.out::println);
        System.out.println("end 1");
        // 1-1000 取后10个
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        System.out.println("end 2");
        // 子句为true一直取，false退出
        Flux.range(1, 100).takeWhile(i -> i < 13).subscribe(System.out::println);
        System.out.println("end 3");
        // 知道 第20个元素时取
        Flux.range(1, 30).takeUntil(i -> i >= 20).subscribe(System.out::println);
    }
    
    // 类比2个字符串zip
    public static void operationZip() {
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
    }
    
    //类似于 buffer 返回值类型是 Flux<flux>
    public static void operationWindow() {
        Flux.range(1, 100).window(20).subscribe(integerFlux -> {
            // filter只能有1个subscribe,没有subscribe的链条 不会触发执行
            integerFlux.filter(i -> {
                // 不会打印
                System.out.println(i);
                return i % 2 != 0;
            });
            integerFlux.filter(i -> i % 2 == 0).subscribe(x -> {
                System.out.println("first:" + x);
            });
            
        });
        // 10个数1个集合，取10次集合
        Flux.interval(Duration.ofMillis(100)).window(8).take(10).toStream().forEach(longFlux -> {
            longFlux.subscribe(System.out::println);
        });
    }
    
    // 过滤 很好理解
    public static void operationFilter() {
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
    }
    
    // 积攒一些元素再向后传播
    public static void operationBuff() {
        // 20个 20个的取
        //Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        // 从0开始 每1秒输出1个元素，该元素是累加的，攒10秒后将元素向后传，take(2)表示只取2次
        Flux.interval(Duration.ofMillis(1000)).buffer(Duration.ofMillis(2000)).take(2).toStream().forEach(System.out::println);
        // 条件,元素整除3 则累计的向后传播
        Flux.range(10, 20).bufferUntil(i -> i % 3 == 0).subscribe(System.out::println);
        // 条件,元素整除3 整除的元素向后传播
        Flux.range(10, 20).bufferWhile(i -> i % 3 == 0).subscribe(System.out::println);
    }
    
    public static void createMono() {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
        // 忽略消息源只产生结果
        Mono.ignoreElements(Flux.just("Hello", "World")).subscribe(System.out::println);
    }
    
    // Flux 是一个能够发出 0 到 N 个元素的标准的 Publisher，
    // 它会被一个 "error"  或 "completion" 信号终止。
    // 因此，一个 Flux 的结果可能是一个 value、completion 或 error。
    // 就像在响应式流规范中规定的那样，这三种类型的信号被翻译为面向下游的 onNext，onComplete和onError方法。
    public static void createFlux() {
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        
        System.out.println("----");
        Flux.generate(sink -> {
            // 只能用一次next
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);
        
        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            // next时就已经往后传播了
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
        
        
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                // 可用多次
                // next调用时，就已经开始有println了
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }
}
