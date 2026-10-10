import { PricePipe, formatPrice } from './price.pipe';

// The Czech number format separates thousands with a non-breaking space
const plain = (text: string) => text.replace(/\s/g, ' ');

describe('PricePipe', () => {
  it('should format whole prices without decimals', () => {
    expect(plain(formatPrice(24990))).toBe('24 990 Kč');
  });

  it('should keep two decimals for other prices', () => {
    expect(plain(formatPrice(249.5))).toBe('249,50 Kč');
  });

  it('should present zero as free unless a number is asked for', () => {
    const pipe = new PricePipe();
    expect(pipe.transform(0)).toBe('Free');
    expect(plain(pipe.transform(0, true))).toBe('0 Kč');
  });

  it('should print nothing for a missing price', () => {
    expect(formatPrice(null)).toBe('');
    expect(formatPrice(undefined)).toBe('');
  });
});
