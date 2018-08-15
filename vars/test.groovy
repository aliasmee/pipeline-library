x = ''
y = 3
tag = x ?: y
println(tag)

if (tag) {
  println("x is max y")
} else {
  println("x is small y")
}
