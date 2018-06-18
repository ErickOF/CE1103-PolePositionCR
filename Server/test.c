#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int count_words(char str[]) {
	printf("Count\n");
	int count = 0;
	char *pch;
	pch = strtok(str,",");
	while (pch != NULL) {
		count++;
		pch = strtok(NULL, ",");
    }
    return count;
}

void split_str(char str[]) {
    int count = count_words(str);

    printf("Size %d\n", count);

    char *words[count];
    for (int i = 0; i < count; ++i) {
        words[i] = (char*) malloc(30*sizeof(char));
    }
  
    char *pch;
    pch = strtok(str,",");
  
    while (pch != NULL) {
        printf("%s\n",pch);
        pch = strtok(NULL, ",");
    }

    for (int i = 0; i < count; ++i) {
        printf("%s\n", words[i]);
    }

    for (int i = 0; i < count; ++i){
        free(words[i]);
    }
}

int main() {
	char str[] = "ErickOF,red";
	split_str(str);
}