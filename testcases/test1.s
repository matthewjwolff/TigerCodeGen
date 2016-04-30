PROCEDURE tigermain
# Before canonicalization: 
MOVE(
 TEMP $v0,
 ESEQ(
  MOVE(
   TEMP t33,
   CALL(
    NAME _initArray,
     CONST 10,
     CONST 0)),
  TEMP t33))
# After canonicalization: 
MOVE(
 TEMP t33,
 CALL(
  NAME _initArray,
   CONST 10,
   CONST 0))
MOVE(
 TEMP $v0,
 TEMP t33)
# Basic Blocks: 
#
LABEL L1
MOVE(
 TEMP t33,
 CALL(
  NAME _initArray,
   CONST 10,
   CONST 0))
MOVE(
 TEMP $v0,
 TEMP t33)
JUMP(
 NAME L0)
LABEL L0
# Trace Scheduled: 
LABEL L1
MOVE(
 TEMP t33,
 CALL(
  NAME _initArray,
   CONST 10,
   CONST 0))
MOVE(
 TEMP $v0,
 TEMP t33)
JUMP(
 NAME L0)
LABEL L0
# Instructions: 
L1:
	li t34 10
	move $a0 t34
	move $a1 $0
	jal _initArray
	lw t33 $v0
	lw $v0 t33
	b L0
	la t35 L0
	jr t35
L0:
END tigermain
