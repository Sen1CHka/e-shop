import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

/**
 * Product picture. Products without a photo (or with a broken link) get
 * a coloured tile with the icon of their category, the colour is derived from the name.
 */
@Component({
  selector: 'selling-product-image',
  standalone: true,
  imports: [CommonModule],
  template: `
    <img *ngIf="imageUrl && !broken; else placeholder" [src]="imageUrl" [alt]="name" loading="lazy" (error)="broken = true" />
    <ng-template #placeholder>
      <div class="placeholder" [style.background]="background" role="img" [attr.aria-label]="name">
        <span aria-hidden="true">{{ icon || '📦' }}</span>
      </div>
    </ng-template>
  `,
  styles: [
    `
      :host {
        display: block;
        aspect-ratio: 4 / 3;
        overflow: hidden;
        border-radius: inherit;
        container-type: inline-size;
      }
      img,
      .placeholder {
        width: 100%;
        height: 100%;
      }
      img {
        object-fit: cover;
      }
      .placeholder {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 3rem;
        font-size: 28cqw;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SellingProductImageComponent {
  @Input({ required: true })
  name!: string;

  @Input()
  imageUrl: string | null = null;

  @Input()
  icon: string | null = null;

  broken = false;

  get background(): string {
    let hash = 0;
    for (const char of this.name) {
      hash = (hash * 31 + char.charCodeAt(0)) % 360;
    }
    return `linear-gradient(135deg, hsl(${hash} 70% 92%), hsl(${(hash + 40) % 360} 70% 84%))`;
  }
}
