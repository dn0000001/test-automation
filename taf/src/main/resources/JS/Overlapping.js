rect1 = arguments[0].getBoundingClientRect();
rect2 = arguments[1].getBoundingClientRect();
return !(
  rect1.right < rect2.left ||
  rect1.left > rect2.right ||
  rect1.bottom < rect2.top ||
  rect1.top > rect2.bottom
);
