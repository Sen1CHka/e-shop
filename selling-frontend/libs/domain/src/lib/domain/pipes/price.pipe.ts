import { Pipe, PipeTransform } from '@angular/core';

const whole = new Intl.NumberFormat('cs-CZ', { maximumFractionDigits: 0 });
const decimal = new Intl.NumberFormat('cs-CZ', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

export function formatPrice(value: number | null | undefined): string {
  if (value === null || value === undefined) {
    return '';
  }
  if (value === 0) {
    return 'Free';
  }
  return (Number.isInteger(value) ? whole : decimal).format(value) + ' Kč';
}

@Pipe({
  name: 'price',
  standalone: true,
})
export class PricePipe implements PipeTransform {
  transform(value: number | null | undefined, zeroAsNumber = false): string {
    return zeroAsNumber && value === 0 ? '0 Kč' : formatPrice(value);
  }
}
