
CREATE TABLE [dbo].[Prescription_Transaction_New](
	[Pre_id] [decimal](18, 0) NOT NULL,
	[P_id] [decimal](18, 0) NOT NULL,
	[sym_nm] [varchar](6000) NULL,
	[Med_nm] [varchar](6000) NULL,
	[Ex_date] [smalldatetime] NULL,
	[Fee_Code] [char](5) NULL,
	[PaidAmt] [decimal](18, 0) NULL,
	[OS_Bal] [decimal](18, 0) NULL,
	[Net_Payable] [decimal](18, 0) NULL,
	[Recevied_Amt] [decimal](18, 0) NULL,
	primary key (Pre_id, P_id)
) 
GO

INSERT INTO Prescription_Transaction_New select * FROM Prescription_Transaction
GO

DROP TABLE Prescription_Transaction
GO

CREATE TABLE [dbo].[Prescription_Transaction](
	[Pre_id] [decimal](18, 0) NOT NULL,
	[P_id] [decimal](18, 0) NOT NULL,
	[sym_nm] [varchar](6000) NULL,
	[Med_nm] [varchar](6000) NULL,
	[Ex_date] [smalldatetime] NULL,
	[Fee_Code] [char](5) NULL,
	[PaidAmt] [decimal](18, 0) NULL,
	[OS_Bal] [decimal](18, 0) NULL,
	[Net_Payable] [decimal](18, 0) NULL,
	[Recevied_Amt] [decimal](18, 0) NULL,
	primary key (Pre_id, P_id)
) 
GO

INSERT INTO Prescription_Transaction select * FROM Prescription_Transaction_New
GO

DROP TABLE Prescription_Transaction_New
GO