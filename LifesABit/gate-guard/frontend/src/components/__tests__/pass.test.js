import {render, screen, cleanup} from '@testing-library/react';
import PassComponent from '../PassComponent';

test('should render pass (date)', () => {
    render( <PassComponent
            passID= "testID"
            firstName= "Joe"
            lastName= "Shmo"
            usageBased= "false"
            expirationDate= "1/1/2001"
            email= 'joeshmo@email.com'/>);

    // expect(passElement).toBeInTheDocument();
    const passElemName = screen.getByTestId('pass-name'); 
    expect(passElemName.innerHTML).toBe('Joe Shmo');

    const passElemExp = screen.getByTestId('pass-exp'); 
    expect(passElemExp.lastElementChild.textContent).toBe('Exp: 1/1/2001');
})
