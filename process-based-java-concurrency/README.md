# What do read?

- https://www.baeldung.com/linux/fork-vfork-exec-clone

- https://linux.die.net/man/3/exec                        // family of 6 functions with various args
- https://linux.die.net/man/3/execl
- https://linux.die.net/man/3/execlp
- https://linux.die.net/man/3/execv
- https://linux.die.net/man/3/execvp
- https://linux.die.net/man/3/execle
- https://linux.die.net/man/2/execve                      // this one is actually a system call, while all others are stdlib's helpers/wrappers/derivatives
- https://linux.die.net/man/2/vfork
- https://linux.die.net/man/2/fork
- https://linux.die.net/man/2/clone

# How to read man URL?
- 2: System calls (functions provided by the kernel)
- 3: Library calls (functions from standard C library, libc)

# How to trace JVM?

```bash
strace -f -p XXXX -e trace=exec,execl,execlp,execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execl,execlp,execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execlp,execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execve,vfork,fork,clone
```

# What is the morale?

- Don't be a dinosaur. Keep learning. Be open-minded. Be curious. Stay up-to-date. Deep dive.
- Long time ago, process-based concurrency was aggressively & massively replaced with kernel-thread-based concurrency.
- In Java/JVM terms, process-based concurrency was replaced with platform-thread-based concurrency.
- Evolution & history are cyclic.
- Most likely, in the following decade, Java's platform-thread-based concurrency will be replaced with Java's virtual-thread-based concurrency.
