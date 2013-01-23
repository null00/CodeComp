segment .text
global	strlen

strlen:
	push	edi
	xor	ecx, ecx
	mov	edi, [esp+8]
	not	ecx
	xor	al, al
	cld
repne	scasb
	not	ecx
	pop	edi
	lea	eax, [ecx-1]
	ret
