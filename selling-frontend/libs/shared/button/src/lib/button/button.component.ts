/* eslint-disable @angular-eslint/component-selector */
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'selling-button',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './button.component.html',
  styleUrl: './button.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ButtonComponent {
  
  @Input()
  buttonColor?: string;

  @Output()
  textColorEvent = new EventEmitter<string>();

  onClickHandler(){
    this.buttonColor = 'green';
    this.textColorEvent.emit(this.buttonColor);
  }
}
