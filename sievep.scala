import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

object Sieve{

 def main(args: Array[ String ]) = {		

  assert (args.length == 2, "must have two arguments. number of primes required, number of threads to run")

  val t0 = java.lang.System.currentTimeMillis()
  val N = args(0).toInt // number of primes required
  val T = args(1).toInt // number of threads to run
  val primes = new AtomicIntegerArray(N) // will hold the primes

  primes.set(0,2)

  var nextSlot = new AtomicInteger(1) // next free slot in primes
  var next = new AtomicInteger(3) // next candidate prime to consider
  
  for( a <- 1 until T){

	var e = new Eratosthenes(next, nextSlot, N, primes)
	e.start
	e.join

  }

  println (primes.get(primes.length()-1))
  println ("Time taken: "+(java.lang.System.currentTimeMillis()-t0))
  

 }

 class Eratosthenes(var next: AtomicInteger, var nextSlot: AtomicInteger, var N: Int, var primes: AtomicIntegerArray) extends Thread{

  override def run() = {		
  
   while(nextSlot.get() < N){
   
   // Test if next is prime;
   // invariant : next is coprime with primes[0..i ) && p = primes(i)
   
    var i = 0; var p = primes.get(i)
   
    primes.synchronized {

     while(p*p<=next.get() && next.get()%p != 0){ i += 1; p = primes.get(i) }

     if(p*p>next.get()){ // next is prime

      primes.set( nextSlot.get(), next.get()); 
      nextSlot.getAndIncrement()

     }

     next.getAndIncrement();

    }
   }
  } 
 }
}

