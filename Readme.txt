Assumption and limitation

1. The text file means files whose extension is txt. 
This simplified the design of checking file type function. 
To check text based file, I will consider to check mimetype of the file.

2. To simplify the function to find out all the words from a file. I assume
the content of the file will be ASCII only and the rule to split each line of input
is simplified to just considering split by space, comma and full stop.
So eg. if content contains "test-test", it will be considered as one word.
The program is able to handle ASCII characters from 33-127. So the array size in 
TRIE node is 95. That will cover all 0-9, A-Z, a-z and most of character you can find
from keyboard on your computer.

3. I assume the search function needs to dealing with large size of text file
with limited memory space. So choose to use TRIE as data structure to save the content.

4. I assume Match means there is a word in the file 100% match target.
And multiple match in the file won't affect search result. So, it really does not
make sense to pass duplicated words as input. 



Improvement
1. Test need to be modified to run on Linux based system.
2. It is really just a data structure design practice. Building the TRIE and discard it
after used once is a waster of time. 
3. For small file, use simple hash structure, you can get better performance.
4. Some methods I did not create junit test. So test coverage is not idea.


Big O
Space O(L)  L is the longest word in each file.
Time O(M)   M is the longest word in the search keyword list.


