//컴퓨터학과 20180971 박주희
#include <stdio.h>
#define MAX_STACK_SIZE 3
typedef int element;
typedef struct {
	element stack[MAX_STACK_SIZE];
	int top;
} StackType;

void init(StackType *s) {
	s->top = -1;
}
int is_empty(StackType *s) {
	return (s->top == -1);
}
int is_full(StackType *s) {
	return (s->top == (MAX_STACK_SIZE - 1));
}
void push(StackType *s, element item) {
	if (is_full(s)) {
		fprintf(stderr, "스택 포화 에러\n");
	}
	else
		s->stack[++(s->top)] = item;
}
element pop(StackType *s) {
	if (is_empty(s)) {
		fprintf(stderr, "스택 포화 에러\n");
		exit(1);
	}
	else
		return s->stack[(s->top)--];
}
element peek(StackType *s) {
	if (is_empty(s)) {
		fprintf(stderr, "스택 포화 에러\n");
		exit(1);

	}
	else
		return s->stack[s->top];
}
void stack_print(StackType *s){

	int i;

	if (is_empty(s) == 1)
		printf("<empty>\n--\n");
	else {
		for (i = s->top; i > -1; i--) {
			if (i == s->top)
				printf("%d <- top\n", s->stack[i]);
			else
				printf("%d\n", s->stack[i]);
		}
		printf("--\n");
	}
}
int main(void) {

	StackType s;

	init(&s);

	stack_print(&s);

	push(&s, 10);
	stack_print(&s);

	push(&s, 20);
	stack_print(&s);

	push(&s, 30);
	stack_print(&s);

	push(&s, 40);
	stack_print(&s);

	pop(&s);
	stack_print(&s);

	push(&s, 50);
	stack_print(&s);

	pop(&s);
	stack_print(&s);

	pop(&s);
	stack_print(&s);

	pop(&s);
	stack_print(&s);

}
