# Huffman-Coding

Directions to Compile & Run:
(Note: [filename] is placeholder for actual filename, such as myTest in myTest.txt and myTest.hzip)
1. in terminal, navigate to /Huffman folder and run 'javac -d out *.java'
2. paste the [filename].txt file in this new /Huffman/out directory (i.e., folder with compiled classes)
3. in terminal, navigate to /Huffman/out directory and run 'java Compress [filename].txt'
4. verify encoded [filename].hzip file has been output to /Huffman/out folder
5. [optional] delete original [filename].txt that was encoded (else will still be overwritten by Decompress)
6. in terminal, run 'java Decompress [filename].hzip' within /Huffman/out directory
7. validate that decoded [filename].txt exists in /Huffman/out directory as expected
