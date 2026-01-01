# What do read?

- https://www.baeldung.com/linux/fork-vfork-exec-clone

- https://linux.die.net/man/3/exec                        // family of 6 functions with various args
- https://linux.die.net/man/3/execl
- https://linux.die.net/man/3/execlp
- https://linux.die.net/man/3/execv
- https://linux.die.net/man/3/execvp
- https://linux.die.net/man/3/execle
- https://linux.die.net/man/3/execve                      // this one is actually a system call, while all others are stdlib's helpers/wrappers/derivatives
- https://linux.die.net/man/3/vfork
- https://linux.die.net/man/3/fork
- https://linux.die.net/man/3/clone

```bash
strace -f -p XXXX -e trace=exec,execl,execlp,execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execl,execlp,execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execlp,execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execv,execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execvp,execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execle,execve,vfork,fork,clone
strace -f -p XXXX -e trace=execve,vfork,fork,clone
```
