#!/usr/bin/perl
# ###########################################################################
# A Perl script to split and XML file into separate named objects.
# Allows more graular modifications to exensively customized SP systems.
# ###########################################################################
use strict;

print "SailPoint XML Object Splitting Utility.\n";

my $xmlHeader = '';
my $lineNumber = 0;
my $inFile = $ARGV[0];

if (!defined $inFile) {
  print "Usage: $0 [SailPoint-Export-Name.xml] [splitOutDir] \n\n";
  print "  If no splitOutDir named, creates a sub-directory called 'splitOut' for output.";
  print "  Objects should be exported with the -clean option (no ID values). \n\n";
  exit;
}

my $outDir = "./splitOut";
if (defined $ARGV[1]) {
  $outDir = $ARGV[1];
}
if (!(-d $outDir)) {
  mkdir $outDir or die "Failed to create directory $outDir \n";
}

my $outFileName;
my $line4spacesFlag = 0;

my $objType;
my $objectName;
my $objectNameHy;

my $baseObjStartRegex = qr/^\<(\S+)\s+/;    
my $baseObjEndRegex;

my $skipRecord = 0;

open INFILE, "<$inFile" or die "Failed to open file $inFile\n";
while (<INFILE>) {
  $lineNumber++;
  my $thisLine = $_;
  $thisLine =~ s/\r//;
  $thisLine =~ s/\n//;

  if (($lineNumber == 4) && ($thisLine =~ /^  / )) {
    # print "Line 4: [$thisLine] \n";
    $line4spacesFlag = 1;
    $baseObjStartRegex = qr/^  \<(\S+)\s+/;    
    if ($thisLine =~ /$baseObjStartRegex/) {
    #if ($thisLine =~ /^  \<(\S+)\s+/) {
      print "Found base object: $1 \n";
    } else {
      print "No base object: $thisLine \n";
    }
  } else {
    $baseObjStartRegex = qr/^\<(\S+)\s+/;    
  }

  if ($thisLine =~ /id=\"/) {
    print "Warning: 'id=' found in the XML, did you export with '-clean'?\n";
  }
  
  if ($lineNumber <= 3) {
    $xmlHeader .= $thisLine . "\n";
  } elsif ($thisLine =~ /$baseObjStartRegex/ ) {
     $objType = $1;
     print " - Found Object Type: $objType \n";

     if ($line4spacesFlag) {
       $baseObjEndRegex   = qr/^  \<\/$objType\>/;
     } else {
       $baseObjEndRegex   = qr/^\<\/$objType\>/;
     }
     
     if (($line4spacesFlag == 0 &&
          $thisLine =~ /^\<$objType.*name=\"((\w|\,|\.|\)|\(|\–|\-|\s|:|;|\&|\_|\/|\\)+)\"/) ||
         ($line4spacesFlag == 1 &&
          $thisLine =~ /^\  <$objType.*name=\"((\w|\,|\.|\)|\(|\–|\-|\s|:|;|\&|\_|\/|\\)+)\"/)) {
        $objectName = $1;
        # print "Object Name: $objectName \n";
        $objectNameHy = $objectName;
        $objectNameHy =~ s/\//-/g;
        $objectNameHy =~ s/\\/-/g;
        $objectNameHy =~ s/ - /-/g;
        $objectNameHy =~ s/\&amp\;/and/g;
        $objectNameHy =~ s/\&apos\;/-/g;
        $objectNameHy =~ s/ /-/g;
        $objectNameHy =~ s/:/-/g;
        
        $outFileName = "$outDir/$objType" . '-' . $objectNameHy . '.xml';
        print " -   file name: $outFileName \n";
        
        open OFILE, ">$outFileName" or die "Failed to open: $outFileName\n";
        print OFILE $xmlHeader;
        print OFILE "$thisLine\n";
     } elsif (($objType eq 'JasperResult') ||
              ($objType eq 'Request')) {
       
	print " -   Skipping object of type $objType. \n";
        $skipRecord = 1;
        
     } else {
        print "ERROR: unexpected line: [$thisLine] \n";
        die "ERROR: Something is broken in the XML input.\n";
     } 
  } elsif ($thisLine =~ /$baseObjEndRegex/ ) {
    if (!$skipRecord) {
      # print "End of output file for $objectName. \n";
      print OFILE "$thisLine\n";
      print OFILE "</sailpoint>\n";
      close OFILE;
   } else {
      # print "End of skip for $objType. \n";
   }
   $skipRecord = 0;
  } else {
    # print "This line: $thisLine \n";
   if (!$skipRecord) {
     print OFILE $thisLine . "\n";
   }
  }
  # if ($lineNumber > 10) {exit;}
  # print "Line: $thisLine \n";
}
close INFILE;
print "\nXML Split out operation complete.\n\n";

