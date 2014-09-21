package lambda.utils;

/**
 * Bounds.
 */
public class Bound {
  public int x, y, width, height;

  public Bound(int height, int width, int x, int y) {
    this.height = height;
    this.width = width;
    this.x = x;
    this.y = y;
  }

  public void union(Bound bound, int dx, int dy) {
    x = Math.min(x, dx + bound.x);
    y = Math.min(y, dy + bound.y);
    width = Math.max(x + width, dx + bound.x + bound.width) - x;
    height = Math.max(y + height, dy + bound.y + bound.height) - y;
  }

  public boolean contains(int x, int y) {
    return this.x <= x && this.x + width > x && this.y <= y && this.y + height > y;
  }
}
