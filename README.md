# SearchAndReplace
Console application in java, for searching and replacing strings in files

A console application that searches for a substring and replaces it with a new substring.
Call parameters:
-p [dir or file] -m [ext1;ext2] -s [oldstr newstr].

Parameter -p, you can specify a directory or a file; if a directory is specified, the search runs through all files in the directory and subdirectories.
Example 1: -p d:\test, example 2: -p d:\test\file.txt.

The -m parameter allows you to specify a list of file extensions when searching directories, separated by semicolons.
Example: -m *.tst ;*.txt.

The -s parameter allows you to specify the string to search for and the string to replace with.
Example 1: old _str new _str - search and replace.
Example 2: -s search _str - only searches and outputs to the terminal files and lines in which it is found.