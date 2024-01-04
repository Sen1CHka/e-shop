import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'keyOrFn',
    pure: false,
    standalone: true,
})
export class KeyOrFunctionPipe implements PipeTransform {
    transform(keyOrFn: any, row: any): string {
        if (keyOrFn instanceof Function) {
            return keyOrFn(row);
        }
        else if (typeof keyOrFn === 'string') {
            const value = row[keyOrFn];
            if (value !== null && value !== undefined){
                return value;
            }
        }
        return '';
    }
}
