	.text
	.globl main
_macros:
	.macro println()
	la $a0 newline
	li $v0 4
	syscall
	.end_macro
main:
	j _endgetSign
getSign:
	li $v0 0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	subu $sp $sp 4 # Pushing return address
	sw $ra ($sp)
	li $v0 9
	li $a0 13
	syscall
	li $t0 8
	sw $t0 ($v0)
	li $t0 'P'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 's'
	sb $t0 6($v0)
	li $t0 'i'
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'i'
	sb $t0 9($v0)
	li $t0 'v'
	sb $t0 10($v0)
	li $t0 'e'
	sb $t0 11($v0)
	li $t0 ' '
	sb $t0 12($v0)
	sw $v0 4($sp)
	# Compiling BinOp components
	lw $v0 8($sp) # Getting local variable x
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 0
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _E
	beq $v0 0 _0
	li $v0 9
	li $a0 9
	syscall
	li $t0 4
	sw $t0 ($v0)
	li $t0 'Z'
	sb $t0 4($v0)
	li $t0 'e'
	sb $t0 5($v0)
	li $t0 'r'
	sb $t0 6($v0)
	li $t0 'o'
	sb $t0 7($v0)
	li $t0 ' '
	sb $t0 8($v0)
	sw $v0 4($sp)
_0:
	# Compiling BinOp components
	lw $v0 8($sp) # Getting local variable x
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 0
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _LT
	beq $v0 0 _1
	li $v0 9
	li $a0 13
	syscall
	li $t0 8
	sw $t0 ($v0)
	li $t0 'N'
	sb $t0 4($v0)
	li $t0 'e'
	sb $t0 5($v0)
	li $t0 'g'
	sb $t0 6($v0)
	li $t0 'a'
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'i'
	sb $t0 9($v0)
	li $t0 'v'
	sb $t0 10($v0)
	li $t0 'e'
	sb $t0 11($v0)
	li $t0 ' '
	sb $t0 12($v0)
	sw $v0 4($sp)
_1:
	lw $ra 0($sp)
	lw $v0 4($sp)
	# Clearing parameters/local variables and return address from stack
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	jr $ra
_endgetSign:
	j _endmax
max:
	li $v0 0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	subu $sp $sp 4 # Pushing return address
	sw $ra ($sp)
	lw $v0 12($sp) # Getting local variable x
	sw $v0 4($sp)
	# Compiling BinOp components
	lw $v0 8($sp) # Getting local variable y
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	lw $v0 16($sp) # Getting local variable x
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _GT
	beq $v0 0 _2
	lw $v0 8($sp) # Getting local variable y
	sw $v0 4($sp)
_2:
	li $v0 1
	subu $v0 $zero $v0
	sw $v0 12($sp)
	li $v0 1
	subu $v0 $zero $v0
	sw $v0 8($sp)
	lw $ra 0($sp)
	lw $v0 4($sp)
	# Clearing parameters/local variables and return address from stack
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	lw $a0 ($sp) # Popping $a0
	addu $sp $sp 4
	jr $ra
_endmax:
	li $v0 9
	li $a0 37
	syscall
	li $t0 32
	sw $t0 ($v0)
	li $t0 'N'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'w'
	sb $t0 6($v0)
	li $t0 ' '
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'e'
	sb $t0 9($v0)
	li $t0 's'
	sb $t0 10($v0)
	li $t0 't'
	sb $t0 11($v0)
	li $t0 'i'
	sb $t0 12($v0)
	li $t0 'n'
	sb $t0 13($v0)
	li $t0 'g'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 '\''
	sb $t0 16($v0)
	li $t0 'g'
	sb $t0 17($v0)
	li $t0 'e'
	sb $t0 18($v0)
	li $t0 't'
	sb $t0 19($v0)
	li $t0 'S'
	sb $t0 20($v0)
	li $t0 'i'
	sb $t0 21($v0)
	li $t0 'g'
	sb $t0 22($v0)
	li $t0 'n'
	sb $t0 23($v0)
	li $t0 '\''
	sb $t0 24($v0)
	li $t0 ' '
	sb $t0 25($v0)
	li $t0 's'
	sb $t0 26($v0)
	li $t0 'u'
	sb $t0 27($v0)
	li $t0 'b'
	sb $t0 28($v0)
	li $t0 'r'
	sb $t0 29($v0)
	li $t0 'o'
	sb $t0 30($v0)
	li $t0 'u'
	sb $t0 31($v0)
	li $t0 't'
	sb $t0 32($v0)
	li $t0 'i'
	sb $t0 33($v0)
	li $t0 'n'
	sb $t0 34($v0)
	li $t0 'e'
	sb $t0 35($v0)
	li $t0 ' '
	sb $t0 36($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 1
	subu $v0 $zero $v0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	jal getSign
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	jal getSign
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 100
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	jal getSign
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 9
	li $a0 33
	syscall
	li $t0 28
	sw $t0 ($v0)
	li $t0 'N'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'w'
	sb $t0 6($v0)
	li $t0 ' '
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'e'
	sb $t0 9($v0)
	li $t0 's'
	sb $t0 10($v0)
	li $t0 't'
	sb $t0 11($v0)
	li $t0 'i'
	sb $t0 12($v0)
	li $t0 'n'
	sb $t0 13($v0)
	li $t0 'g'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 '\''
	sb $t0 16($v0)
	li $t0 'm'
	sb $t0 17($v0)
	li $t0 'a'
	sb $t0 18($v0)
	li $t0 'x'
	sb $t0 19($v0)
	li $t0 '\''
	sb $t0 20($v0)
	li $t0 ' '
	sb $t0 21($v0)
	li $t0 's'
	sb $t0 22($v0)
	li $t0 'u'
	sb $t0 23($v0)
	li $t0 'b'
	sb $t0 24($v0)
	li $t0 'r'
	sb $t0 25($v0)
	li $t0 'o'
	sb $t0 26($v0)
	li $t0 'u'
	sb $t0 27($v0)
	li $t0 't'
	sb $t0 28($v0)
	li $t0 'i'
	sb $t0 29($v0)
	li $t0 'n'
	sb $t0 30($v0)
	li $t0 'e'
	sb $t0 31($v0)
	li $t0 ' '
	sb $t0 32($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 3
	sw $v0 _data_x
	li $v0 4
	sw $v0 _data_y
	lw $v0 _data_x# Getting global variable x
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	lw $v0 _data_y# Getting global variable y
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	jal max
	move $a0 $v0
	li $v0 1
	syscall
	println()
	lw $v0 _data_x# Getting global variable x
	move $a0 $v0
	li $v0 1
	syscall
	println()
	li $v0 9
	li $a0 47
	syscall
	li $t0 42
	sw $t0 ($v0)
	li $t0 'N'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'w'
	sb $t0 6($v0)
	li $t0 ' '
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'e'
	sb $t0 9($v0)
	li $t0 's'
	sb $t0 10($v0)
	li $t0 't'
	sb $t0 11($v0)
	li $t0 'i'
	sb $t0 12($v0)
	li $t0 'n'
	sb $t0 13($v0)
	li $t0 'g'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 'f'
	sb $t0 16($v0)
	li $t0 'o'
	sb $t0 17($v0)
	li $t0 'r'
	sb $t0 18($v0)
	li $t0 '/'
	sb $t0 19($v0)
	li $t0 'w'
	sb $t0 20($v0)
	li $t0 'h'
	sb $t0 21($v0)
	li $t0 'i'
	sb $t0 22($v0)
	li $t0 'l'
	sb $t0 23($v0)
	li $t0 'e'
	sb $t0 24($v0)
	li $t0 ' '
	sb $t0 25($v0)
	li $t0 'l'
	sb $t0 26($v0)
	li $t0 'o'
	sb $t0 27($v0)
	li $t0 'o'
	sb $t0 28($v0)
	li $t0 'p'
	sb $t0 29($v0)
	li $t0 's'
	sb $t0 30($v0)
	li $t0 ' '
	sb $t0 31($v0)
	li $t0 'a'
	sb $t0 32($v0)
	li $t0 'n'
	sb $t0 33($v0)
	li $t0 'd'
	sb $t0 34($v0)
	li $t0 ' '
	sb $t0 35($v0)
	li $t0 'u'
	sb $t0 36($v0)
	li $t0 's'
	sb $t0 37($v0)
	li $t0 'e'
	sb $t0 38($v0)
	li $t0 'r'
	sb $t0 39($v0)
	li $t0 ' '
	sb $t0 40($v0)
	li $t0 'i'
	sb $t0 41($v0)
	li $t0 'n'
	sb $t0 42($v0)
	li $t0 'p'
	sb $t0 43($v0)
	li $t0 'u'
	sb $t0 44($v0)
	li $t0 't'
	sb $t0 45($v0)
	li $t0 ' '
	sb $t0 46($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 9
	li $a0 38
	syscall
	li $t0 33
	sw $t0 ($v0)
	li $t0 'E'
	sb $t0 4($v0)
	li $t0 'n'
	sb $t0 5($v0)
	li $t0 't'
	sb $t0 6($v0)
	li $t0 'e'
	sb $t0 7($v0)
	li $t0 'r'
	sb $t0 8($v0)
	li $t0 ' '
	sb $t0 9($v0)
	li $t0 'a'
	sb $t0 10($v0)
	li $t0 ' '
	sb $t0 11($v0)
	li $t0 'n'
	sb $t0 12($v0)
	li $t0 'u'
	sb $t0 13($v0)
	li $t0 'm'
	sb $t0 14($v0)
	li $t0 'b'
	sb $t0 15($v0)
	li $t0 'e'
	sb $t0 16($v0)
	li $t0 'r'
	sb $t0 17($v0)
	li $t0 ' '
	sb $t0 18($v0)
	li $t0 'f'
	sb $t0 19($v0)
	li $t0 'o'
	sb $t0 20($v0)
	li $t0 'r'
	sb $t0 21($v0)
	li $t0 ' '
	sb $t0 22($v0)
	li $t0 't'
	sb $t0 23($v0)
	li $t0 'h'
	sb $t0 24($v0)
	li $t0 'e'
	sb $t0 25($v0)
	li $t0 ' '
	sb $t0 26($v0)
	li $t0 'f'
	sb $t0 27($v0)
	li $t0 'o'
	sb $t0 28($v0)
	li $t0 'r'
	sb $t0 29($v0)
	li $t0 ' '
	sb $t0 30($v0)
	li $t0 'l'
	sb $t0 31($v0)
	li $t0 'o'
	sb $t0 32($v0)
	li $t0 'o'
	sb $t0 33($v0)
	li $t0 'p'
	sb $t0 34($v0)
	li $t0 ':'
	sb $t0 35($v0)
	li $t0 ' '
	sb $t0 36($v0)
	li $t0 ' '
	sb $t0 37($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 5
	syscall
	sw $v0 _data_input
	li $v0 0
	sw $v0 _data_i
	# Compiling BinOp components
	lw $v0 _data_i# Getting global variable i
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	lw $v0 _data_input# Getting global variable input
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _LT
_3:
	beq $v0 0 _4
	# Compiling BinOp components
	lw $v0 _data_i# Getting global variable i
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	addu $v0 $t0 $v0
	move $a0 $v0
	li $v0 1
	syscall
	println()
	# Compiling BinOp components
	lw $v0 _data_i# Getting global variable i
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	addu $v0 $t0 $v0
	sw $v0 _data_i
	# Compiling BinOp components
	lw $v0 _data_i# Getting global variable i
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	lw $v0 _data_input# Getting global variable input
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _LT
	j _3
_4:
	li $v0 9
	li $a0 40
	syscall
	li $t0 35
	sw $t0 ($v0)
	li $t0 'E'
	sb $t0 4($v0)
	li $t0 'n'
	sb $t0 5($v0)
	li $t0 't'
	sb $t0 6($v0)
	li $t0 'e'
	sb $t0 7($v0)
	li $t0 'r'
	sb $t0 8($v0)
	li $t0 ' '
	sb $t0 9($v0)
	li $t0 'a'
	sb $t0 10($v0)
	li $t0 ' '
	sb $t0 11($v0)
	li $t0 'n'
	sb $t0 12($v0)
	li $t0 'u'
	sb $t0 13($v0)
	li $t0 'm'
	sb $t0 14($v0)
	li $t0 'b'
	sb $t0 15($v0)
	li $t0 'e'
	sb $t0 16($v0)
	li $t0 'r'
	sb $t0 17($v0)
	li $t0 ' '
	sb $t0 18($v0)
	li $t0 'f'
	sb $t0 19($v0)
	li $t0 'o'
	sb $t0 20($v0)
	li $t0 'r'
	sb $t0 21($v0)
	li $t0 ' '
	sb $t0 22($v0)
	li $t0 't'
	sb $t0 23($v0)
	li $t0 'h'
	sb $t0 24($v0)
	li $t0 'e'
	sb $t0 25($v0)
	li $t0 ' '
	sb $t0 26($v0)
	li $t0 'w'
	sb $t0 27($v0)
	li $t0 'h'
	sb $t0 28($v0)
	li $t0 'i'
	sb $t0 29($v0)
	li $t0 'l'
	sb $t0 30($v0)
	li $t0 'e'
	sb $t0 31($v0)
	li $t0 ' '
	sb $t0 32($v0)
	li $t0 'l'
	sb $t0 33($v0)
	li $t0 'o'
	sb $t0 34($v0)
	li $t0 'o'
	sb $t0 35($v0)
	li $t0 'p'
	sb $t0 36($v0)
	li $t0 ':'
	sb $t0 37($v0)
	li $t0 ' '
	sb $t0 38($v0)
	li $t0 ' '
	sb $t0 39($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 5
	syscall
	sw $v0 _data_input
	li $v0 0
	sw $v0 _data_index
	# Compiling BinOp components
	lw $v0 _data_index# Getting global variable index
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	lw $v0 _data_input# Getting global variable input
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _LT
_5:
	beq $v0 0 _6
	# Compiling BinOp components
	lw $v0 _data_index# Getting global variable index
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 1
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	addu $v0 $t0 $v0
	sw $v0 _data_index
	lw $v0 _data_index# Getting global variable index
	move $a0 $v0
	li $v0 1
	syscall
	println()
	# Compiling BinOp components
	lw $v0 _data_index# Getting global variable index
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	lw $v0 _data_input# Getting global variable input
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _LT
	j _5
_6:
	li $v0 9
	li $a0 30
	syscall
	li $t0 25
	sw $t0 ($v0)
	li $t0 'N'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'w'
	sb $t0 6($v0)
	li $t0 ' '
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'e'
	sb $t0 9($v0)
	li $t0 's'
	sb $t0 10($v0)
	li $t0 't'
	sb $t0 11($v0)
	li $t0 'i'
	sb $t0 12($v0)
	li $t0 'n'
	sb $t0 13($v0)
	li $t0 'g'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 'i'
	sb $t0 16($v0)
	li $t0 'f'
	sb $t0 17($v0)
	li $t0 ' '
	sb $t0 18($v0)
	li $t0 's'
	sb $t0 19($v0)
	li $t0 't'
	sb $t0 20($v0)
	li $t0 'a'
	sb $t0 21($v0)
	li $t0 't'
	sb $t0 22($v0)
	li $t0 'e'
	sb $t0 23($v0)
	li $t0 'm'
	sb $t0 24($v0)
	li $t0 'e'
	sb $t0 25($v0)
	li $t0 'n'
	sb $t0 26($v0)
	li $t0 't'
	sb $t0 27($v0)
	li $t0 's'
	sb $t0 28($v0)
	li $t0 ' '
	sb $t0 29($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 9
	li $a0 21
	syscall
	li $t0 16
	sw $t0 ($v0)
	li $t0 'E'
	sb $t0 4($v0)
	li $t0 'n'
	sb $t0 5($v0)
	li $t0 't'
	sb $t0 6($v0)
	li $t0 'e'
	sb $t0 7($v0)
	li $t0 'r'
	sb $t0 8($v0)
	li $t0 ' '
	sb $t0 9($v0)
	li $t0 'a'
	sb $t0 10($v0)
	li $t0 ' '
	sb $t0 11($v0)
	li $t0 'n'
	sb $t0 12($v0)
	li $t0 'u'
	sb $t0 13($v0)
	li $t0 'm'
	sb $t0 14($v0)
	li $t0 'b'
	sb $t0 15($v0)
	li $t0 'e'
	sb $t0 16($v0)
	li $t0 'r'
	sb $t0 17($v0)
	li $t0 ':'
	sb $t0 18($v0)
	li $t0 ' '
	sb $t0 19($v0)
	li $t0 ' '
	sb $t0 20($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 5
	syscall
	sw $v0 _data_input
	# Compiling BinOp components
	# Compiling BinOp components
	lw $v0 _data_input# Getting global variable input
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 7
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	divu $v0 $t0
	mfhi $v0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 0
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _E
	beq $v0 0 _7
	li $v0 9
	li $a0 34
	syscall
	li $t0 29
	sw $t0 ($v0)
	li $t0 'Y'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'u'
	sb $t0 6($v0)
	li $t0 'r'
	sb $t0 7($v0)
	li $t0 ' '
	sb $t0 8($v0)
	li $t0 'n'
	sb $t0 9($v0)
	li $t0 'u'
	sb $t0 10($v0)
	li $t0 'm'
	sb $t0 11($v0)
	li $t0 'b'
	sb $t0 12($v0)
	li $t0 'e'
	sb $t0 13($v0)
	li $t0 'r'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 'i'
	sb $t0 16($v0)
	li $t0 's'
	sb $t0 17($v0)
	li $t0 ' '
	sb $t0 18($v0)
	li $t0 'd'
	sb $t0 19($v0)
	li $t0 'i'
	sb $t0 20($v0)
	li $t0 'v'
	sb $t0 21($v0)
	li $t0 'i'
	sb $t0 22($v0)
	li $t0 's'
	sb $t0 23($v0)
	li $t0 'i'
	sb $t0 24($v0)
	li $t0 'b'
	sb $t0 25($v0)
	li $t0 'l'
	sb $t0 26($v0)
	li $t0 'e'
	sb $t0 27($v0)
	li $t0 ' '
	sb $t0 28($v0)
	li $t0 'b'
	sb $t0 29($v0)
	li $t0 'y'
	sb $t0 30($v0)
	li $t0 ' '
	sb $t0 31($v0)
	li $t0 '7'
	sb $t0 32($v0)
	li $t0 ' '
	sb $t0 33($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
_7:
	# Compiling BinOp components
	# Compiling BinOp components
	lw $v0 _data_input# Getting global variable input
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 7
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	divu $v0 $t0
	mfhi $v0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 0
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _NE
	beq $v0 0 _8
	li $v0 9
	li $a0 38
	syscall
	li $t0 33
	sw $t0 ($v0)
	li $t0 'Y'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'u'
	sb $t0 6($v0)
	li $t0 'r'
	sb $t0 7($v0)
	li $t0 ' '
	sb $t0 8($v0)
	li $t0 'n'
	sb $t0 9($v0)
	li $t0 'u'
	sb $t0 10($v0)
	li $t0 'm'
	sb $t0 11($v0)
	li $t0 'b'
	sb $t0 12($v0)
	li $t0 'e'
	sb $t0 13($v0)
	li $t0 'r'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 'i'
	sb $t0 16($v0)
	li $t0 's'
	sb $t0 17($v0)
	li $t0 ' '
	sb $t0 18($v0)
	li $t0 'n'
	sb $t0 19($v0)
	li $t0 'o'
	sb $t0 20($v0)
	li $t0 't'
	sb $t0 21($v0)
	li $t0 ' '
	sb $t0 22($v0)
	li $t0 'd'
	sb $t0 23($v0)
	li $t0 'i'
	sb $t0 24($v0)
	li $t0 'v'
	sb $t0 25($v0)
	li $t0 'i'
	sb $t0 26($v0)
	li $t0 's'
	sb $t0 27($v0)
	li $t0 'i'
	sb $t0 28($v0)
	li $t0 'b'
	sb $t0 29($v0)
	li $t0 'l'
	sb $t0 30($v0)
	li $t0 'e'
	sb $t0 31($v0)
	li $t0 ' '
	sb $t0 32($v0)
	li $t0 'b'
	sb $t0 33($v0)
	li $t0 'y'
	sb $t0 34($v0)
	li $t0 ' '
	sb $t0 35($v0)
	li $t0 '7'
	sb $t0 36($v0)
	li $t0 ' '
	sb $t0 37($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
_8:
	li $v0 9
	li $a0 38
	syscall
	li $t0 33
	sw $t0 ($v0)
	li $t0 'N'
	sb $t0 4($v0)
	li $t0 'o'
	sb $t0 5($v0)
	li $t0 'w'
	sb $t0 6($v0)
	li $t0 ' '
	sb $t0 7($v0)
	li $t0 't'
	sb $t0 8($v0)
	li $t0 'e'
	sb $t0 9($v0)
	li $t0 's'
	sb $t0 10($v0)
	li $t0 't'
	sb $t0 11($v0)
	li $t0 'i'
	sb $t0 12($v0)
	li $t0 'n'
	sb $t0 13($v0)
	li $t0 'g'
	sb $t0 14($v0)
	li $t0 ' '
	sb $t0 15($v0)
	li $t0 't'
	sb $t0 16($v0)
	li $t0 'y'
	sb $t0 17($v0)
	li $t0 'p'
	sb $t0 18($v0)
	li $t0 'e'
	sb $t0 19($v0)
	li $t0 's'
	sb $t0 20($v0)
	li $t0 ' '
	sb $t0 21($v0)
	li $t0 'm'
	sb $t0 22($v0)
	li $t0 'o'
	sb $t0 23($v0)
	li $t0 'r'
	sb $t0 24($v0)
	li $t0 'e'
	sb $t0 25($v0)
	li $t0 ' '
	sb $t0 26($v0)
	li $t0 't'
	sb $t0 27($v0)
	li $t0 'h'
	sb $t0 28($v0)
	li $t0 'o'
	sb $t0 29($v0)
	li $t0 'r'
	sb $t0 30($v0)
	li $t0 'o'
	sb $t0 31($v0)
	li $t0 'u'
	sb $t0 32($v0)
	li $t0 'g'
	sb $t0 33($v0)
	li $t0 'h'
	sb $t0 34($v0)
	li $t0 'l'
	sb $t0 35($v0)
	li $t0 'y'
	sb $t0 36($v0)
	li $t0 ' '
	sb $t0 37($v0)
	move $a0 $v0
	addu $a0 $a0 4
	li $v0 4
	syscall
	println()
	li $v0 1
	sw $v0 _data_a
	li $v0 4
	sw $v0 _data_b
	# Compiling BinOp components
	lw $v0 _data_b# Getting global variable b
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 5
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _E
	sw $v0 _data_a
	# Compiling BinOp components
	lw $v0 _data_a# Getting global variable a
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	# Compiling BinOp components
	# Compiling BinOp components
	lw $v0 _data_b# Getting global variable b
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 4
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	subu $v0 $t0 $v0
	subu $sp $sp 4 # Pushing $v0
	sw $v0 ($sp)
	li $v0 0
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	jal _E
	lw $t0 ($sp) # Popping $t0
	addu $sp $sp 4
	# Beginning BinOp compilation
	or $v0 $t0 $v0
	sw $v0 _data_c
	lw $v0 _data_c# Getting global variable c
	move $a0 $v0
	li $v0 1
	syscall
	println()
	li $v0 10
	syscall
_TRUE:
	li $v0 1
	jr $ra
_FALSE:
	li $v0 0
	jr $ra
_LEQ:
	ble $t0 $v0 _TRUE
	j _FALSE
_LT:
	blt $t0 $v0 _TRUE
	j _FALSE
_GEQ:
	bge $t0 $v0 _TRUE
	j _FALSE
_GT:
	bgt $t0 $v0 _TRUE
	j _FALSE
_E:
	beq $t0 $v0 _TRUE
	j _FALSE
_NE:
	bne $t0 $v0 _TRUE
	j _FALSE
	.data
	_data_input: .word 0
	_data_a: .word 0
	_data_b: .word 0
	_data_c: .word 0
	_data_x: .word 0
	_data_y: .word 0
	_data_i: .word 0
	_data_index: .word 0
	newline: .asciiz "\n"
