// package com.YeuTech.Application.Mappers;

// import com.YeuTech.Domain.Entities.PasswordResetToken;
// import com.YeuTech.Dtos.Request.ForgotPasswordRequestDto;
// // import com.YeuTech.Dtos.Request.ResetPasswordRequestDto;
// // import com.YeuTech.Dtos.Request.VerifyOtpRequestDto;
// import com.YeuTech.Domain.Repository.IUserRepository;

// public class PasswordResetServiceMapper {

//     private final IUserRepository _userRepository;

//     public PasswordResetServiceMapper(IUserRepository userRepository) {
//         this._userRepository = userRepository;
//     }

//     public static PasswordResetToken toDomain(ForgotPasswordRequestDto dto) {
//         if (dto == null)
//             return null;
//         PasswordResetToken token = new PasswordResetToken();
//         token.setEmail(f);
//         return token;
//     }

// }
