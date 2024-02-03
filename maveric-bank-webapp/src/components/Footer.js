import React from 'react';
import mavericFooter from '../assets/maveric-logo-white.png'

function Footer() {
  return (
    <div className='footer'>
        <span><img className='footer-img' src={mavericFooter} alt="Maveric" /> </span>
        <span className='footer-text'>Copyright @2022 Maveric Systems India Pvt Ltd</span>
    </div>
  )
}

export default Footer