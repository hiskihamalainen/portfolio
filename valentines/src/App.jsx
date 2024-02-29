import React, { useState } from 'react';

function ValentineProposal() {
  const [showQuestion, setShowQuestion] = useState(true);
  const [showMessage, setShowMessage] = useState(false);
  const [showError, setShowError] = useState(false);

  const handleNoClick = () => {
    setShowQuestion(false);
    setShowError(true);
  };

  const handleYesClick = () => {
    setShowQuestion(false);
    setShowError(false);
    setShowMessage(true);
  };

  const handleTryAgainClick = () => {
    setShowError(false);
    setShowQuestion(true);
  };

  return (
    <div style={{ backgroundColor: 'pink', width: '100vw', height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', textAlign: 'center' }}>
      {showQuestion && (
        <div>
          <p>
          <span style={{ fontSize: '24px', color: '#000000', padding: '10px', borderRadius: '10px' }}>Do you want to be my Valentine?</span> 
          </p>
          <button onClick={handleNoClick}>No</button>
          <button onClick={handleYesClick}>Yes</button>
        </div>
      )}
      {showError && (
        <div>
          <p>
          <span style={{ fontSize: '24px', color: '#000000', padding: '10px', borderRadius: '10px' }}>That's impossible.</span>
            </p>
          <button onClick={handleTryAgainClick}>Try Again</button>
        </div>
      )}
      {showMessage && (
        <div style={{ position: 'relative' }}>
          <div style={{
            width: '200px', // Adjust width as needed
            height: '200px', // Adjust height as needed
            borderRadius: '50%',
            backgroundColor: '#e8ccff', // Adjust color intensity
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            marginBottom: '20px',
            border: '4px solid #8a4eff', // Add border
          }}>
            <span style={{ fontSize: '24px', color: '#000000', padding: '10px', borderRadius: '10px' }}>Perfect <span role="img" aria-label="heart">❤️</span> ask Hiski for further details</span>
          </div>
        </div>
      )}
    </div>
  );
}

export default ValentineProposal;
