import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

object Sieve{

 def main(args: Array[ String ]) = {		

  assert (args.length == 1, "must have one argument")

  val t0 = java.lang.System.currentTimeMillis()
  val N = args(0).toInt // number of primes required
  val primes = new AtomicIntegerArray(N) // will hold the primes

  primes.set(0,2)

  var nextSlot = new AtomicInteger(1) // next free slot in primes
  var next = new AtomicInteger(3) // next candidate prime to consider
  
  var e1 = new Eratosthenes(next, nextSlot, N, primes)
  var e2 = new Eratosthenes(next, nextSlot, N, primes)

  e1.start
  e2.start

  e1.join
  e2.join

  println (primes.get(primes.length()-1))
  println ("Time taken: "+(java.lang.System.currentTimeMillis()-t0))
  

 }

 class Eratosthenes(var next: AtomicInteger, var nextSlot: AtomicInteger, var N: Int, var primes: AtomicIntegerArray) extends Thread{

  override def run() = {		
  primes.synchronized {
  while(nextSlot.get() < N){
   
   // Test if next is prime;
   // invariant : next is coprime with primes[0..i ) && p = primes(i)
   
   var i = 0; var p = primes.get(i)
   
    while(p*p<=next.get() && next.get()%p != 0){ i += 1; p = primes.get(i) }

     if(p*p>next.get()){ // next is prime

      primes.set( nextSlot.get() , next.get()); 
      nextSlot.getAndIncrement()

     }

    next.getAndIncrement();

   }
}
 } 
}
}

