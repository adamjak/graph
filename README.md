Graph
=====

Author: Tomáš Adamják

Web: [thomas.adamjak.net](https://thomas.adamjak.net)

Documentation
-------------
Javadoc: [adamjak.github.io/graph](https://adamjak.github.io/graph)

License
-------

Copyright (c) 2015-2017, Tomáš Adamják

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of graph nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Requirements
------------

* Java 8
* Maven 3
* gcc 4.9+


Usage
-----

### Debian

1. `git clone [repository]`
2. `cd graph`
3. `sudo ln -s [JDK_HOME]/include/linux/jni_md.h [JDK_HOME]/include/jni_md.h`
4. `mvn -P hpux clean package`
5. `export LD_LIBRARY_PATH=native/hpux/target`
6. `sudo execstack -c native/hpux/target/libkowaliknative.so`
7. `java -jar application/target/application-0.2-SNAPSHOT.jar [param]`
