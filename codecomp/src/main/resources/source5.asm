;@author Mateusz Opala
;ping pong

data segment
	string_game_over db "GAME OVER",10,13,"$"
	racket   dw   ?	;Po³o¿enie rakietki
	ball_y 	 dw   ?	;wspolrzedna y pilki
	ball_x 	 dw   ?	;wspolrzedna x pilki
	ball2_y	 dw	  ? ;wspolrzedna y drugiej pilki
	ball2_x  dw   ? ;wspolrzedna x drugiej pilki
	vector_y dw   ?	;przesuniecie wertykalne pilki
	vector_x dw	  ? ;przesuniecie horyzontalne pilki
	vector2_y dw  ? ;przesuniecie wertykalne drugiej pilki
	vector2_x dw  ? ;przesuniecie horyzontalne pilki
	tmp     dw   ?
data ends

myStack segment STACK
	db 200 dup(?)
	top db ?
myStack ends

code segment
	start:
		main proc
		
			;segment stosu
			mov ax,seg top
			mov ss,ax
			mov sp,offset top
			;segment danych
			mov ax,seg data
			mov ds,ax
			;inicjalizacja zmiennych
			mov word ptr ds:[racket],90
			mov word ptr ds:[ball_y],1
			mov word ptr ds:[ball_x],1
			mov word ptr ds:[ball2_y],100
			mov word ptr ds:[ball2_x],1
			mov word ptr ds:[vector_y],2
			mov word ptr ds:[vector_x],2
			mov word ptr ds:[vector2_y], 1
			mov word ptr ds:[vector2_x], 1
			call graph			;Przechodzê do trybu graficznego 320x200
			call draw_racket	;Rysuje kulkê i paletkê
			call draw_ball
			
			main_loop:
				call sleep
				call keystroke
				ball_section:
					call translate_ball
					call cls	;czycimy ekran do rysowania
					call draw_racket
					call draw_ball
					call translate_ball2
					call draw_ball2
			jmp main_loop
			jmp exit
			
			;przesuwa kulkê, zmienia zwrot kulki i ewentualnie wy³¹cza program
			translate_ball:
				mov ax,word ptr ds:[ball_y]	
				add ax,word ptr ds:[vector_y]
				mov word ptr ds:[ball_y],ax
				cmp ax,0					;Czy nie zderzam siê z dachem
				ja row_above_zero
				mov word ptr ds:[vector_y],2
				row_above_zero:
				cmp ax,198					;czy nie odbija sie od ziemi
				jb cols
				mov word ptr ds:[vector_y],-1
				cols:						
				mov ax,word ptr ds:[ball_x]
				add ax,word ptr ds:[vector_x]
				mov word ptr ds:[ball_x],ax
				cmp ax,0					;czy nie odbija sie od lewego brzegu
				ja col_above_zero
				mov word ptr ds:[vector_x],2
				col_above_zero:
				cmp ax,317					;czy nie prawy brzeg -> game over
				jae exit
				cmp ax,310					;czy nie odbija sie od rakiety
				jb no_racket
				mov ax,word ptr ds:[ball_y]	;czy lezy miedzy koncami rakiety
				add ax,2
				cmp ax,word ptr ds:[racket]
				jb no_racket
				mov ax,word ptr ds:[racket]
				add ax,29	;dodaje d³ugosæ rakiety
				cmp ax,word ptr ds:[ball_y]
				jb no_racket
				mov word ptr ds:[vector_x],-1
				no_racket:
				ret
				
			;analogicznie jak wy¿ej
			translate_ball2:
				mov ax,word ptr ds:[ball2_y]
				add ax,word ptr ds:[vector2_y]
				mov word ptr ds:[ball2_y],ax
				cmp ax,0					
				ja row_above2_zero
				mov word ptr ds:[vector2_y],2
				row_above2_zero:
				cmp ax,198					
				jb cols2
				mov word ptr ds:[vector2_y],-2
				cols2:						
				mov ax,word ptr ds:[ball2_x]
				add ax,word ptr ds:[vector2_x]
				mov word ptr ds:[ball2_x],ax
				cmp ax,0					
				ja col_above2_zero
				mov word ptr ds:[vector2_x],2
				col_above2_zero:
				cmp ax,317					
				jae exit
				cmp ax,310					
				jb no_racket2
				mov ax,word ptr ds:[ball2_y]	
				add ax,2
				cmp ax,word ptr ds:[racket]
				jb no_racket2
				mov ax,word ptr ds:[racket]
				add ax,29	
				cmp ax,word ptr ds:[ball2_y]
				jb no_racket2
				mov word ptr ds:[vector2_x],-2
				no_racket2:
				ret
			
			;przesuwaj¹ paletkê zgodnie z wciniêtym klawiszem
			translate_racket_down:
				mov ax,word ptr ds:[racket]
				add ax,5
				cmp ax,170
				jae dnc
				mov word ptr ds:[racket],ax
				jmp ball_section
				dnc:
				mov word ptr ds:[racket],170
				jmp ball_section
			translate_racket_up:
				mov ax,word ptr ds:[racket]
				sub ax,5
				cmp ax,170
				jae dnc2
				mov word ptr ds:[racket],ax
				jmp ball_section
				dnc2:
				mov word ptr ds:[racket],0
				jmp ball_section
			;sprawdza klikniêty przycisk na klawiaturze i podejmuje jak¹ akcjê
			keystroke:
				xor ax,ax
				mov ah,01h
				
				
				
				int 16h
				jz ball_section
				xor ax,ax
				int 16h	;W buforze jest znak, wiêc go pobieram
				cmp ah,01h	;Escape?
				je exit
				cmp ah,4bh	; <--
				je translate_racket_down
				cmp ah,4dh	; -->
				je translate_racket_up
				jmp ball_section
			;sleep
			sleep:
				xor cx,cx		
				mov ah,86h
				mov dx,20000
				int 15h		
			;rysuje rakietkê w po³o¿eniu z racket
			draw_racket:
				mov ax,word ptr ds:[racket]
				call set_reg
				mov cx,30	;d³ugoæ rakietki
				loop1:
					add di,312
					push cx
					mov cx,8
					call draw_line
					pop cx
					loop loop1
				ret
			;rysuje "pi³kê" w po³o¿eniu z ball[x][y]
			draw_ball:
				mov ax,word ptr ds:[ball_y]
				call set_reg
				add di,word ptr ds:[ball_x]
				mov cx,3
				call draw_line
				mov cx,3
				add di,317
				call draw_line
				mov cx,3
				add di,317
				call draw_line
				ret
			;rysuje "pi³kê" w po³o¿eniu z ball2[x][y]
			draw_ball2:
				mov ax,word ptr ds:[ball2_y]
				call set_reg
				add di,word ptr ds:[ball2_x]
				mov cx,3
				call draw_line
				mov cx,3
				add di,317
				call draw_line
				mov cx,3
				add di,317
				call draw_line
				ret
			
			;ustawia rejestry
			set_reg:
				mov word ptr ds:[tmp],ax
				mov ax,word ptr ds:[tmp]
				mov cx,320
				mul cx
				mov di,ax
				mov al,12	;kolor
				ret
			;czyci ekran
			cls:
				xor cx,cx	;lewy górny róg
				xor bx,bx	
				mov dx,63999;prawy dolny
				mov	ah,06h	;przewijanie
				mov	al,0	;czyszczenie
				int	10h	
				ret 
			;rysuje liniê
			draw_line:
				mov byte ptr es:[di],al
				inc di
				loop draw_line
				ret
			;wypisuje game over
			game_over:
				mov dx,offset string_game_over
				mov ah,9
				int 21h
				ret
			;czeka a¿ klawisz zostanie naciniêty
			readkey:
				xor ah,ah
				int 16h
				ret
			;prze³¹cza program do trybu graficznego 320x200
			graph:
				mov al,013h
				xor ah,ah
				int 10h
				mov ax,0a000h
				mov es,ax
				ret
			;wraca do trybu tekstowego
			text:
				mov al,03h
				xor ah,ah
				int 10h
				ret
			;koñczy program
			exit:
				call text
				call game_over
				mov ah,04ch
				int 21h
		main endp
	code ends
end start